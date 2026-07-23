package com.redmath.lecture02.security;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface OauthUserResolver {

  OauthUser resolve(
      OAuth2AuthenticationToken authentication);

}