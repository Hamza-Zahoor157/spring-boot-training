package com.redmath.Lecture02.user;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApiUserService implements UserDetailsService {

    private final ApiUserRepository repository;

    public ApiUserService(ApiUserRepository repository) {
        this.repository = repository;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        ApiUser apiUser = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

        return User.withUsername(apiUser.getUserName())
                .password(apiUser.getPassword())
                .authorities(apiUser.getRoles())
                .build();
    }
}
