package org.repocrud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.repocrud.history.Auditable;
import org.repocrud.repository.converters.AuthorityListToStringConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Entity(name = "crud_user")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class User extends Auditable implements UserDetails {

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @NotNull
    @Email
    private String email;

    private boolean enabled = true;

    @ElementCollection
    private List<String> roles;

    @Convert(converter = AuthorityListToStringConverter.class)
    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    @Enumerated(EnumType.STRING)
    private Language locale = Language.RUSSIAN;

    @ManyToOne
    protected Company company;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CrudHistory> crudHistories;


    public User(String username, String encodedPassword, List<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = encodedPassword;
        this.authorities = authorities.stream()
                .map(a -> (GrantedAuthority) a)
                .collect(Collectors.toList());

    }


//    @Override
//    public List<GrantedAuthority> getAuthorities() {
//        if (authorities == null || authorities.length() == 0) {
//            return Collections.emptyList();
//        }
//        return Stream.of(authorities.split(";")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }


    @Override
    public String toString() {
        return username;
    }
}
