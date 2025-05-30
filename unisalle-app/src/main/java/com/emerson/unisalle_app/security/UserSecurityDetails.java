package com.emerson.unisalle_app.security;

import com.emerson.unisalle_app.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSecurityDetails implements UserDetails {

    private final User user;
    public UserSecurityDetails(User user){
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Set<String> authorities = StringUtils.commaDelimitedListToSet(this.user.getAuthorities());
        return authorities.stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority)).collect(Collectors.toSet());
    }

    @Override
    public String getPassword( ){
        return this.user.getPassword();
    }

    @Override
    public String getUsername( ){
        return this.user.getEmail();
    }
}
