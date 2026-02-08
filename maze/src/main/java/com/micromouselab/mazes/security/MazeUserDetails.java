package com.micromouselab.mazes.security;


import com.micromouselab.mazes.domain.Role;
import com.micromouselab.mazes.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MazeUserDetails implements UserDetails {

    private final User user;

    public MazeUserDetails(User user){
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role userRole = user.getRole();
        String roleString = "ROLE_" + userRole.toString();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleString);
        List<SimpleGrantedAuthority> authorities = List.of(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
