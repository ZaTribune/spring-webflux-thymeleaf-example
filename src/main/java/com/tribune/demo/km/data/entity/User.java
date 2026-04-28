package com.tribune.demo.km.data.entity;

import com.tribune.demo.km.config.ObjectIdHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Document
public class User implements UserDetails {

    @Id
    @JsonSerialize(using = ObjectIdHandler.Serializer.class)
    @JsonDeserialize(using = ObjectIdHandler.Deserializer.class)
    private ObjectId id;

    @Email
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)// by default hibernate on the database creates this column with a length of 255
    private String password;

    @Transient
    private String passwordConfirm;

    // Store role names directly instead of DBRef to support reactive queries
    // Example: {"ROLE_ADMIN", "ROLE_USER"}
    private Set<String> roleNames = new HashSet<>();

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNotExpired;

    private Boolean enabled;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleNames == null) {
            return new HashSet<>();
        }
        return roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
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


