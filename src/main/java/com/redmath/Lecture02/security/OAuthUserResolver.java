package com.redmath.Lecture02.security;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface OAuthUserResolver {

    OAuthUser resolve(
            OAuth2AuthenticationToken authentication);

}