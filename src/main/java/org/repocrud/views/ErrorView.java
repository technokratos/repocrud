package org.repocrud.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Route("error")
public class ErrorView extends Div {


    @PostConstruct
    public void init() {
        log.info("Open error page");
        UI.getCurrent().addAfterNavigationListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
    }

}