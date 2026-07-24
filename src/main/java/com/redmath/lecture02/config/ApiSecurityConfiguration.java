package com.redmath.lecture02.config;

import com.redmath.lecture02.security.ApiSecurityService;
import com.redmath.lecture02.security.FormLoginSuccessHandler;
import com.redmath.lecture02.security.OauthAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 */
@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

  @Bean
  public NimbusJwtDecoder jwtDecoder(ApiSecurityService apiSecurityService) {
    return apiSecurityService.jwtDecoder();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler,
      FormLoginSuccessHandler formLoginSuccessHandler) throws Exception {

    return http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/**")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/login/**",
                "/oauth2/**")
            .permitAll()
            .requestMatchers(HttpMethod.GET,
                "/api/v1/news",
                "/api/v1/news/**")
            .permitAll()
            .anyRequest()
            .authenticated())
        .formLogin(form -> form
            .successHandler(formLoginSuccessHandler))
        .oauth2Login(oauth ->
            oauth.successHandler(oauthAuthenticationSuccessHandler))
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> {
            }))
        .build();
  }
}
