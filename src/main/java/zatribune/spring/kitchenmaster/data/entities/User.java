package zatribune.spring.kitchenmaster.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class User implements UserDetails {
    @Id
    private ObjectId id;

    @Email
    private String username;

    @NotBlank
    @Size(min = 8,max = 255)// by default hibernate on the database creates this column with a length of 255
    private String password;

    @Transient
    private String passwordConfirm;

    @DBRef
    private Set<Role> roles;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNotExpired;

    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
