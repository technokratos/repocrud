package org.repocrud.history;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * @author Denis B. Kulikov<br/>
 * date: 29.09.2018:1:39<br/>
 */
@Getter
@Setter
@MappedSuperclass
public class Identified  {
    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "presetIdGenerator")
    @GenericGenerator(name = "presetIdGenerator", strategy = "org.repocrud.history.UUIDPresetIdGenerator")
    protected UUID id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identified that = (Identified) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}

