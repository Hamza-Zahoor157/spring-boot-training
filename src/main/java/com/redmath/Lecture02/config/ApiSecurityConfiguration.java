package com.redmath.Lecture02.config;

import com.redmath.Lecture02.security.OAuthAuthenticationSuccessHandler;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.security.jwt.secret}") String secret) {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.security.jwt.secret}") String secret) {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String roles = jwt.getClaimAsString("roles");
            return AuthorityUtils.commaSeparatedStringToAuthorityList(roles == null ? "" : roles);
        });
        return converter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter, OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler) throws Exception {

        return http
            .csrf(csrf -> csrf.disable())
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
                // .formLogin(...) is not needed when using Google OAuth login.
                .oauth2Login(oauth ->
                        oauth.successHandler(oAuthAuthenticationSuccessHandler))
//                successHandler((request, response, authentication) -> {
//                    String username = authentication.getName();
//                    if (authentication instanceof OAuth2AuthenticationToken oauth2Authentication) {
//                        Object email = oauth2Authentication.getPrincipal().getAttributes().get("email");
//                        if (email instanceof String emailValue && !emailValue.isBlank()) {
//                            username = emailValue;
//                        }
//                    }
//
//                    try {
//                        String token = userService.generateToken(username);
//                        response.sendRedirect("/?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8));
//                    } catch (UsernameNotFoundException ex) {
//                        response.sendRedirect("/?error=no_local_user&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8));
//                    }
//                }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }
}
