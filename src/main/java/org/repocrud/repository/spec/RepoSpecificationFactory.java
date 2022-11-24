package org.repocrud.repository.spec;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.repocrud.config.SecurityUtils;
import org.repocrud.crud.ForiegnKey;
import org.repocrud.domain.Company;
import org.repocrud.domain.User;
import org.repocrud.history.Auditable;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Denis B. Kulikov<br/>
 * date: 14.02.2019:14:31<br/>
 */
@Slf4j
public class RepoSpecificationFactory {

    public static <T> Specification<T> getFilterSpecification(final Class<T> domainType, final T filterInstance, List<String> visibleProperties) {
        Specification<T> filter = (e, cq, cb) -> {
            Predicate[] predicates = visibleProperties.stream()
                    .map(propertyName -> getFilterPredicate(domainType, filterInstance, e, cb, propertyName))
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new);

            return predicates.length == 1 ? predicates[0] : cb.and(predicates);
        };
        User userDetails = (User) SecurityUtils.getUserDetails();
        if (userDetails.getCompany() != null && domainType.getSuperclass() == Auditable.class) {
            return filter.and(getCompanyRestriction(userDetails.getCompany()));
        } else {
            return filter;
        }
    }


    static <T> Predicate
    getFilterPredicate(Class<T> domainType, T filterInstance, Root<T> e, CriteriaBuilder cb, String propertyName) {
        PropertyDescriptor propertyDescriptor =
                BeanUtils.getPropertyDescriptor(domainType, propertyName);
        if (propertyDescriptor != null) {
            try {
                Object value = propertyDescriptor.getReadMethod().invoke(filterInstance);
                if (value == null) {
                    return null;
                }
                javax.persistence.criteria.Path<String> propertyPath = e.get(propertyName);
                if (propertyDescriptor.getPropertyType() == String.class) {
                    return cb.like(propertyPath, "%" + value.toString() + "%");
                } else {
                    return cb.equal(propertyPath, value);
                }
            } catch (IllegalAccessException | InvocationTargetException exception) {
                log.error("Error in create filter", exception);
            }

        } else {
            log.error("Not found propertyDescriptor for {}, for property {}", domainType, propertyName);
        }
        return null;
    }

    public static <T> Specification getForeignKeySpecification(ForiegnKey<T> foriegnKey) {
        return (e, cq, cb) -> getForiegnKeyEqual(foriegnKey, e, cb);
    }

    private static <T> Predicate getForiegnKeyEqual(ForiegnKey<T> foriegnKey, Root e, CriteriaBuilder cb) {
        return cb.equal(e.get(foriegnKey.getForeignField().getName()), foriegnKey.getId());
    }

    public static <T> Specification<T> getFilterSpecification(final Class<T> domainType,
                                                              final T filterInstance, List<String> visibleProperties, @NotNull ForiegnKey<T> foriegnKey) {
        return (Specification<T>) (e, cq, cb) -> {
            if (filterInstance != null) {
                Predicate[] predicates = visibleProperties.stream()
                        .map(propertyName -> getFilterPredicate(domainType, filterInstance, e, cb, propertyName))
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new);

                return cb.and(predicates.length == 1 ? predicates[0] : cb.and(predicates), getForiegnKeyEqual(foriegnKey, e, cb));
            } else {
                return getForiegnKeyEqual(foriegnKey, e, cb);
            }
        };
    }

    public static <T> Specification<T> getCompanyRestriction(Company company) {
        return (e, cq, cb) -> {
            try {
                Path<T> companyPath = e.get("company");
                return cb.or(cb.equal(companyPath, company), cb.isNull(companyPath));
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }

        };
    }
}

