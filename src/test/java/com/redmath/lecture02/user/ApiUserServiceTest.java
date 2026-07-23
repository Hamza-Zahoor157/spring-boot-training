package com.redmath.lecture02.user;

import com.redmath.lecture02.user.ApiUser;
import com.redmath.lecture02.user.ApiUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUserServiceTest {

  private final ApiUserRepository repository = mock(ApiUserRepository.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final ApiUserService service = new ApiUserService(repository, passwordEncoder);

  @Test
  void loadUserByUsername_whenUserExists_returnsUserDetails() {
    ApiUser apiUser = new ApiUser();
    apiUser.setUserName("testuser");
    apiUser.setPassword("encodedPassword");
    apiUser.setRoles("EDITOR,ADMIN");

    when(repository.findByUserName("testuser")).thenReturn(Optional.of(apiUser));

    User userDetails = (User) service.loadUserByUsername("testuser");

    assertEquals("testuser", userDetails.getUsername());
    assertEquals("encodedPassword", userDetails.getPassword());
    assertEquals(2, userDetails.getAuthorities().size());
  }

  @Test
  void loadUserByUsername_whenUserNotFound_throwsException() {
    when(repository.findByUserName("unknown")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      service.loadUserByUsername("unknown");
    });
  }

  @Test
  void getOrCreateUser_whenUserExists_returnsExistingUser() {
    ApiUser existingUser = new ApiUser();
    existingUser.setUserName("existing");
    existingUser.setPassword("pass");
    existingUser.setRoles("EDITOR");

    when(repository.findByUserName("existing")).thenReturn(Optional.of(existingUser));

    ApiUser result = service.getOrCreateUser("existing");

    assertEquals("existing", result.getUserName());
    verify(repository, never()).save(any());
  }

  @Test
  void getOrCreateUser_whenUserDoesNotExist_createsNewUser() {
    when(repository.findByUserName("newuser")).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn("encodedPass");

    ApiUser savedUser = new ApiUser();
    savedUser.setUserName("newuser");
    savedUser.setPassword("encodedPass");
    savedUser.setRoles("REPORTER");
    when(repository.save(any(ApiUser.class))).thenReturn(savedUser);

    ApiUser result = service.getOrCreateUser("newuser");

    assertEquals("newuser", result.getUserName());
    assertEquals("REPORTER", result.getRoles());
    verify(repository).save(any(ApiUser.class));
  }
}
