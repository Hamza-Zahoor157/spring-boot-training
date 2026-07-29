package com.redmath.lecture02.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class ApiUserServiceTest {

  @Autowired
  private ApiUserService apiUserService;

  @Autowired
  private ApiUserRepository apiUserRepository;

  @Test
  void loadUserByUsername_existingUser_returnsUserDetails() {
    ApiUser user = new ApiUser();
    user.setUserName("testuser");
    user.setPassword("encodedpass");
    user.setRoles("REPORTER");
    user.setCreatedAt(java.time.LocalDateTime.now());
    user.setUpdatedAt(java.time.LocalDateTime.now());
    apiUserRepository.save(user);

    UserDetails details = apiUserService.loadUserByUsername("testuser");
    assertNotNull(details);
    assertEquals("testuser", details.getUsername());
  }

  @Test
  void loadUserByUsername_nonExistingUser_throwsUsernameNotFoundException() {
    assertThrows(UsernameNotFoundException.class,
        () -> apiUserService.loadUserByUsername("nonexistent"));
  }

  @Test
  void getByUsername_existingUser_returnsUser() {
    ApiUser user = new ApiUser();
    user.setUserName("finduser");
    user.setPassword("pass");
    user.setRoles("EDITOR");
    user.setCreatedAt(java.time.LocalDateTime.now());
    user.setUpdatedAt(java.time.LocalDateTime.now());
    apiUserRepository.save(user);

    ApiUser found = apiUserService.getByUsername("finduser");
    assertNotNull(found);
    assertEquals("finduser", found.getUserName());
  }

  @Test
  void getOrCreateUser_existingUser_returnsExisting() {
    ApiUser existing = new ApiUser();
    existing.setUserName("existinguser");
    existing.setPassword("oldpass");
    existing.setRoles("REPORTER");
    existing.setCreatedAt(java.time.LocalDateTime.now());
    existing.setUpdatedAt(java.time.LocalDateTime.now());
    apiUserRepository.save(existing);

    ApiUser result = apiUserService.getOrCreateUser("existinguser");
    assertNotNull(result);
    assertEquals("existinguser", result.getUserName());
  }

  @Test
  void getOrCreateUser_newUser_createsAndReturns() {
    ApiUser result = apiUserService.getOrCreateUser("newuser");
    assertNotNull(result);
    assertEquals("newuser", result.getUserName());
    assertEquals("REPORTER", result.getRoles());
    assertNotNull(result.getCreatedAt());
    assertNotNull(result.getUpdatedAt());

    ApiUser inDb = apiUserRepository.findByUserName("newuser").orElse(null);
    assertNotNull(inDb);
  }
}