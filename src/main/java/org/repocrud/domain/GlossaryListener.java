package org.repocrud.domain;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.repocrud.text.LocalText;
import org.springframework.stereotype.Component;

/**
 * @author Denis B. Kulikov<br/>
 * date: 30.09.2018:11:10<br/>
 */
@Component
public class GlossaryListener {

    @PrePersist
    @PreUpdate
    public void preModification(Glossary glossary) {
        LocalText.put(glossary.getKey(), glossary.getValue(), glossary.getLanguage());
    }

}
