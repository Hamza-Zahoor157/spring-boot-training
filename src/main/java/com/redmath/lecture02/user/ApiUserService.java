package com.redmath.lecture02.user;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiUserService implements UserDetailsService {

  private final ApiUserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public ApiUserService(ApiUserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }
  @Cacheable(value = "users", key = "#username")
  public ApiUser getByUsername(String username) {

    log.info("Reading from database...");

    return repository.findByUserName(username).orElseThrow();
  }

  @Override
  @NonNull
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    ApiUser apiUser = repository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

    return User.withUsername(apiUser.getUserName())
        .password(apiUser.getPassword())
        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(
            "SCOPE_" + apiUser.getRoles().toUpperCase().replace(",", ",SCOPE_")))
        .build();
  }

  public ApiUser getOrCreateUser(String username) {

    return repository.findByUserName(username)
        .orElseGet(() -> {
          ApiUser user = new ApiUser();

          user.setUserName(username);

          user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

          user.setRoles("REPORTER");

          user.setCreatedAt(LocalDateTime.now());

          user.setUpdatedAt(LocalDateTime.now());

          return repository.save(user);
        });
  }
}


