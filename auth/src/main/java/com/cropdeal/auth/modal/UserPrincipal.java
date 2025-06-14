package com.cropdeal.auth.modal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;


// ? - Spring Security may need to persist authentication objects across different layers (e.g., session storage, caching mechanisms).
// - If UserPrincipal needs to be transferred over a network (e.g., via REST or messaging), making it Serializable ensures proper data transmission.
public class UserPrincipal implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient UserCredential user1;

    public UserPrincipal(UserCredential user) {
        this.user1 = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user1.getPassword();
    }

    @Override
    public String getUsername() {
        return user1.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}