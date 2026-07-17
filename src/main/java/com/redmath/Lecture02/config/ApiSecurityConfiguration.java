package com.redmath.Lecture02.config;

import com.redmath.Lecture02.user.ApiUser;
import com.redmath.Lecture02.user.ApiUserService;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

    @Bean
        public OpaqueTokenIntrospector opaqueTokenIntrospector(ApiUserService userService) {
                return token -> {
                        ApiUser user = userService.findByToken(token);

                        return new OAuth2IntrospectionAuthenticatedPrincipal(
                                        user.getUserName(),
                                        Map.of("sub", user.getUserName()),
                                        AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
                };
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            ApiUserService userService,
            OpaqueTokenIntrospector opaqueTokenIntrospector) throws Exception {

        return http
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
                .oauth2Login(config -> config.successHandler((request, response, authentication) -> {
                    String username = authentication.getName();
                    if (authentication instanceof OAuth2AuthenticationToken oauth2Authentication) {
                        Object email = oauth2Authentication.getPrincipal().getAttributes().get("email");
                        if (email instanceof String emailValue && !emailValue.isBlank()) {
                            username = emailValue;
                        }
                    }

                    try {
                        ApiUser user = userService.generateToken(username);
                        response.sendRedirect("/?token=" + user.getToken());
                    } catch (UsernameNotFoundException ex) {
                        response.sendRedirect("/?error=no_local_user&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8));
                    }
                }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(opaqueToken -> opaqueToken
                                .introspector(opaqueTokenIntrospector)))
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/api/**"))
                // .securityContext(...) and .sessionManagement(STATELESS) commented out:
                // oauth2Login requires session during OAuth2 authorization flow.
                // .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class) commented out:
                // this cookie helper was only needed for the custom form login flow.
                .build();
    }
}
