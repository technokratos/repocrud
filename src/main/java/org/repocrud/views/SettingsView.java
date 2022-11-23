package org.repocrud.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import org.repocrud.crud.CompanyCrudContainer;
import org.repocrud.crud.GlossaryCrudContainer;
import org.repocrud.crud.SmtpSettingsCrudContainer;
import org.repocrud.crud.UserRepositoryCrud;
import org.repocrud.views.components.TabContainer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.repocrud.text.LocalText.text;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Settings")
public class SettingsView extends VerticalLayout {

    @Autowired
    UserRepositoryCrud userRepositoryCrud;
    @Autowired
    private CompanyCrudContainer companyCrud;
    @Autowired
    private GlossaryCrudContainer glossaryCrud;
    @Autowired
    private SmtpSettingsCrudContainer smtpSettingsCrudContainer;

    @PostConstruct
    private void init() {
        TabContainer mainMenu = new TabContainer(Tabs.Orientation.HORIZONTAL);
        mainMenu.addTab(text("main.user"), userRepositoryCrud);
        mainMenu.addTab(text("main.company"), companyCrud);
        mainMenu.addTab(text("main.glossary"), glossaryCrud);
        mainMenu.addTab(text("main.smpt"), smtpSettingsCrudContainer);
        add(mainMenu);
    }
}