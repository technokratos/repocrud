package org.repocrud.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.repocrud.domain.User;
import org.repocrud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import static org.repocrud.text.LocalText.text;

@Slf4j
@AnonymousAllowed
@Route("registration")
@PageTitle("Registration")
@Getter
@Setter
public class RegistrationView extends VerticalLayout {

    private final Label groupName = new Label();
    private TextField pseudonym = new TextField(text("registration.pseudonym"));
    private TextField email = new TextField(text("registration.email"));
    private PasswordField password = new PasswordField(text("registration.password"));
    private PasswordField confirmPassword = new PasswordField(text("registration.confirmPassword"));
    private Button registration = new Button(text("login.registration"));
    private final Button confidential = new Button(text("registration.confidential"));
    Binder<User> binder = new Binder<>(User.class);

    private User user = new User();

    @Autowired
    private UserDetailsServiceImpl userService;


    public RegistrationView() {

        view();
        validate();
        registration.addClickListener(e -> save());
//        confidential.addClickListener(event -> registration.getUI().ifPresent(ui ->
//                ui.navigate("confidential")));
    }

    public void view() {
        add(groupName);
        pseudonym.setMinWidth("450px");
        email.setMinWidth("450px");
        password.setMinWidth("450px");
        confirmPassword.setMinWidth("450px");
        registration.setMinWidth("450px");
        add(
                pseudonym, email,
                password, confirmPassword,
                registration, confidential
        );
        setAlignItems(Alignment.CENTER);
    }

    private void save() {
        try {
            if (binder.validate().isOk()) {
                binder.writeBean(user);
                userService.saveUserWithEncodePassword(user, user.getPassword());
                registration.getUI().ifPresent(ui ->
                        ui.navigate(""));
            }
        } catch (ValidationException e) {
            e.getValidationErrors().forEach(error -> Notification.show(error.getErrorMessage()));
        } finally {
            System.out.println();
        }

    }

    private void validate() {
        password.setValueChangeMode(ValueChangeMode.EAGER);
        confirmPassword.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(pseudonym)
                // Explicit validator instance
                .asRequired(text("registration.pseudonymShouldBeFilled"))
                .bind(User::getUsername, User::setUsername);

        binder.forField(email)
                // Explicit validator instance
                .withValidator(new EmailValidator(
                        //"This doesn't look like a valid email address"
                        text("registration.isNotEmail")))
                .withValidator((SerializablePredicate<String>) email -> !userService.existsByEmail(email),
                        text("registration.emailIsAlreadyExist", email)
                )
                .bind(User::getEmail, User::setEmail);


        binder.forField(password)
                // Validator defined based on a lambda
                // and an error message
                .withValidator(
                        pass -> pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{4,15}$"),
                        //"Password does not meet the requirements"
                        text("registration.wrongPassword"))
                .bind(User::getPassword, User::setPassword);

        binder.forField(confirmPassword)
                // Validator defined based on a lambda
                // and an error message
                .withValidator(
                        pass -> password.getValue().equals(confirmPassword.getValue()),
                        text("registration.notTheSamePassword"));//"The spanks are not the same"
    }


}

