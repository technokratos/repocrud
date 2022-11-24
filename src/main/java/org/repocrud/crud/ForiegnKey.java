package org.repocrud.crud;

import java.lang.reflect.Field;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Denis B. Kulikov<br/>
 * date: 14.09.2018:9:40<br/>
 */
@Getter
@Slf4j
@ToString
@AllArgsConstructor
public class ForiegnKey<T> {
    private final T filter;
    private final Field foreignField;
    private final UUID id;

    void initForeignKey(T t) {
        try {

            foreignField.set(t, foreignField.get(filter));
        } catch (IllegalAccessException e) {
            log.error("Error set foreign key for nested crud " + foreignField, e);
        }
    }

}
