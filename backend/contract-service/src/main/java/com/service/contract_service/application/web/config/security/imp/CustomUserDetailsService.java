package com.service.contract_service.application.web.config.security.imp;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        if ("basic".equals(username)) {
            return User.withUsername("basic")
                    .password("{noop}123")
                    .roles("BASIC")
                    .build();
        } else if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{noop}123")
                    .roles("ADMIN")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}