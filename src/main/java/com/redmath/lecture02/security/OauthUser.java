package com.redmath.lecture02.security;

public record OauthUser(

    String provider,

    String principal,

    String email,

    String displayName

) {

}