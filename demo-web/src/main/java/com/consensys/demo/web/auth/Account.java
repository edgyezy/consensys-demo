package com.consensys.demo.web.auth;

import com.sun.istack.internal.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Entity
public class Account implements UserDetails {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String username;
    @NotNull
    private String password;
    private boolean enabled = true;
    private boolean locked = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We don't need to support different roles in this demo
        return Collections.emptySet();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return username.equals(account.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
