package org.coffeeshop.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;


/*
    * Custom UserDetails implementation that includes staff ID and authorities.
    * Used by Spring Security for authentication and authorization.
    * @author Umunna David
    * @version 1.0
    * @since 2604/2026

*/
public class StaffUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public StaffUserDetails(
            Long id,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    /*
        * Override methods to expose authorities, username, and password for authentication.
         * Account status methods return true to indicate the account is valid and active.
     */
     @Override
    public Collection getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}