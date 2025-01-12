package com.spam.whidy.common.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String userId;

    public static CustomUserDetails of(Long userId){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.userId = String.valueOf(userId);
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }
}
