package org.repocrud.views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final Button registration = new Button(org.repocrud.text.LocalText.text("login.registration"));

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        initTranslation();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");

//        login.addForgotPasswordListener(event -> registration.getUI().ifPresent(ui ->
//                ui.navigate("sendRestorePasswordEmail")));

        registration.addClickListener(e ->
                registration.getUI().ifPresent(ui ->
                        ui.navigate(RegistrationView.class))
        );


        HorizontalLayout buttons = new HorizontalLayout(registration);
        add(new H1(org.repocrud.text.LocalText.text("login")), login, buttons);

    }

    private void initTranslation() {
        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("");
        i18nForm.setUsername(org.repocrud.text.LocalText.text("login.user"));
        i18nForm.setPassword(org.repocrud.text.LocalText.text("login.password"));
        i18nForm.setSubmit(org.repocrud.text.LocalText.text("login.submit"));
        i18nForm.setForgotPassword(org.repocrud.text.LocalText.text("login.forgotPassword"));
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle(org.repocrud.text.LocalText.text("login.errorLogin"));
        i18nErrorMessage.setMessage(org.repocrud.text.LocalText.text("login.errorMessage"));
        i18n.setErrorMessage(i18nErrorMessage);


        login.setI18n(i18n);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }


}
