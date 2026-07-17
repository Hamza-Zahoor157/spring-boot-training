package com.redmath.Lecture02.user;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(apiUser.getRoles()))
                .build();
    }

    public ApiUser generateToken(String username) {
        ApiUser apiUser = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

        apiUser.setToken(UUID.randomUUID().toString());
        return repository.save(apiUser);
    }

    public ApiUser findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token"),
                        "invalid token"));
    }
}
