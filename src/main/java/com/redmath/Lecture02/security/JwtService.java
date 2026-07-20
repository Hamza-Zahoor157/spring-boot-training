package com.redmath.Lecture02.security;

import com.redmath.Lecture02.user.ApiUser;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(
            JwtEncoder jwtEncoder) {

        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(
            ApiUser apiUser) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(apiUser.getUserName())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .claim("roles", apiUser.getRoles())
                .build();

        return jwtEncoder.encode(
                        JwtEncoderParameters.from(

                                JwsHeader.with(
                                                MacAlgorithm.HS256)
                                        .build(),

                                claims))
                .getTokenValue();
    }
}
