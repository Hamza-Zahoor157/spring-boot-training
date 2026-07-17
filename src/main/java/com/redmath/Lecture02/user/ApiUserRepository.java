package com.redmath.Lecture02.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {

    Optional<ApiUser> findByUserName(String userName);

    Optional<ApiUser> findByToken(String token);
}
