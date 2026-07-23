package com.redmath.lecture02.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {

  Optional<ApiUser> findByUserName(String userName);
}
