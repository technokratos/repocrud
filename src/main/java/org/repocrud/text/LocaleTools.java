package org.repocrud.text;

import com.vaadin.flow.component.UI;
import java.util.Locale;
import org.repocrud.domain.Language;

/**
 * @author Denis B. Kulikov<br/>
 * date: 04.10.2018:22:46<br/>
 */
public class LocaleTools {


    public static Locale getUILocale() {
        UI current = UI.getCurrent();
        return (current == null)? Language.RUSSIAN.getLocale(): current.getLocale();
    }
}
