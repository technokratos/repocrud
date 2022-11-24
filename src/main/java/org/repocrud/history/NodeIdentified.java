package org.repocrud.history;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.repocrud.components.Identifiable;

/**
 * @author Denis B. Kulikov<br/>
 * date: 29.09.2018:1:39<br/>
 */
@Getter
@Setter
@MappedSuperclass
public class NodeIdentified implements Identifiable<NodeId> {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nodeIdGenerator")
    @GenericGenerator(name = "nodeIdGenerator",
            strategy = "org.repocrud.history.NodeIdGenerator"
    )
    protected NodeId id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeIdentified that = (NodeIdentified) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
