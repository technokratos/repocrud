package org.repocrud.crud;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import org.repocrud.annotations.CheckBoxCollection;
import org.repocrud.annotations.SortInList;
import org.repocrud.components.DateField;
import org.repocrud.components.EnumCollectionField;
import org.repocrud.service.ApplicationContextProvider;
import org.springframework.data.domain.Sort;
import org.vaadin.crudui.form.FieldProvider;

/**
 * @author @author Denis B. Kulikov
 */
public class UpdateFieldProvider implements FieldProvider {


    private final Field field;
    private final Class<?> type;

    public UpdateFieldProvider(Class<?> type, Field field) {
        this.type = type;
        this.field = field;
    }

    @Override
    public HasValueAndElement buildField(Object o) {

        if (Boolean.class.isAssignableFrom(type) || boolean.class == type) {
            return new Checkbox();
        }

        if (LocalDate.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type)) {
            return new DatePicker();
        }

        if (Enum.class.isAssignableFrom(type)) {
            Object[] values = type.getEnumConstants();
            ComboBox comboBox = new ComboBox<>();
            comboBox.setItems(Arrays.asList(values));
            return comboBox;
        }

        if (String.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type) || type.isPrimitive()) {
            return new TextField();
        }

        Annotation[] annotations = type.getAnnotations();
        if (Stream.of(annotations).anyMatch(a -> a instanceof Entity)) {
            Optional<Annotation> sort = Stream.of(annotations).filter(annotation -> annotation instanceof SortInList).findFirst();
            final List all;
            if (sort.isPresent()) {
                SortInList sortInList = (SortInList) sort.get();
                all = ApplicationContextProvider.getRepository(type).findAll(Sort.by(sortInList.direction(), sortInList.value()));
            } else {
                all = ApplicationContextProvider.getRepository(type).findAll();
            }
            return new ComboBox("", all);
        }

        if (Collection.class.isAssignableFrom(type)) {
            Annotation[] fieldAnnotation = field.getAnnotations();
            Optional<Annotation> elementAnnotation = Stream.of(fieldAnnotation).filter(annotation -> annotation instanceof ElementCollection).findFirst();
            Optional<Annotation> entityAnnotation = Stream.of(fieldAnnotation).filter(annotation -> annotation instanceof CheckBoxCollection).findFirst();
            if (elementAnnotation.isPresent() && entityAnnotation.isPresent()) {
                return new EnumCollectionField(((ElementCollection) elementAnnotation.get()).targetClass(), (CheckBoxCollection) entityAnnotation.get());
            }

        }

        if (type == LocalDate.class) {
            return new DateField();
        }


        return null;
    }

}