package com.redmath.Lecture02.user;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ApiUserService implements UserDetailsService {

    private final ApiUserRepository repository;

    public ApiUserService(ApiUserRepository repository, JwtEncoder jwtEncoder) {
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

    public ApiUser getOrCreateUser(String username) {

        return repository.findByUserName(username)
                .orElseGet(() -> {
                    ApiUser user = new ApiUser();

                    user.setUserName(username);

                    user.setPassword(UUID.randomUUID().toString());

                    user.setRoles("ROLE_EDITOR");

                    user.setCreatedAt(LocalDateTime.now());

                    user.setUpdatedAt(LocalDateTime.now());

                    return repository.save(user);
                });
    }
}


