package com.redmath.Lecture02.security;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class DefaultOAuthUserResolver
        implements OAuthUserResolver {
    @Override
    public OAuthUser resolve(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        String provider = authentication.getAuthorizedClientRegistrationId();
        return switch (provider) {

            case "google" -> resolveGoogle(user);

            case "github" -> resolveGithub(user);

            default -> throw new IllegalArgumentException(
                    "Unsupported provider: " + provider);
        };

    }

    private OAuthUser resolveGoogle(OAuth2User user) {
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        return new OAuthUser(
                "google",
                email,
                email,
                name
        );
    }


    private OAuthUser resolveGithub(OAuth2User user) {
        String login = user.getAttribute("login");
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String principal =
                email != null ? email : login;
        return new OAuthUser(
                "github",
                login,
                principal,
                name
        );
    }
}
