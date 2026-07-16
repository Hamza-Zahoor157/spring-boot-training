package com.redmath.Lecture02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/news",
                                "/api/v1/news/**")
                        .permitAll()
//                        .requestMatchers(HttpMethod.POST,
//                                "/api/v1/news")
//                        .hasAnyRole("REPORTER", "EDITOR")
//                        .requestMatchers(HttpMethod.PUT,
//                                "/api/v1/news/**")
//                        .hasAnyRole("EDITOR", "REPORTER")
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/api/v1/news/**")
//                        .hasAnyRole("ADMIN","EDITOR")
//                        .anyRequest()
//                        .hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .formLogin(config -> {})
                .httpBasic(basic -> {})
                .csrf(csrf ->  csrf.spa())
                .build();
    }
}
