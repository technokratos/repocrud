package org.repocrud.domain;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author Denis B. Kulikov<br/>
 * date: 21.09.2018:22:27<br/>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class CrudHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;


    private ZonedDateTime time;

    private String domain;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Column(length = 1000)
    private String body;



    public enum Operation {
        SAVE,
        UPDATE,
        LOGIN, LOGOUT, DELETE, REMBER_LOGIN;
    }
}
