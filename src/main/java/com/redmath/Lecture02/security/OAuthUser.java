package com.redmath.Lecture02.security;

public record OAuthUser(

        String provider,

        String principal,

        String email,

        String displayName

) {}