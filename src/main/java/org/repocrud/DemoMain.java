package org.repocrud;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.repocrud.crud.CompanyCrudContainer;
import org.repocrud.crud.GlossaryCrudContainer;
import org.repocrud.crud.SmtpSettingsCrudContainer;
import org.repocrud.crud.UserRepositoryCrud;
import org.repocrud.views.components.TabContainer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.repocrud.text.LocalText.text;


/**
 * @author Denis B. Kulikov<br/>
 * date: 18.03.2019:12:02<br/>
 */
@Slf4j
@PermitAll
@Route("demo")
public class DemoMain extends HorizontalLayout {


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
        TabContainer mainMenu = new TabContainer(Tabs.Orientation.VERTICAL);

        mainMenu.addTab(text("main.user"), userRepositoryCrud);
        mainMenu.addTab(text("main.company"), companyCrud);
        mainMenu.addTab(text("main.glossary"), glossaryCrud);
        mainMenu.addTab(text("main.smpt"), smtpSettingsCrudContainer);
        add(mainMenu);
    }
}

