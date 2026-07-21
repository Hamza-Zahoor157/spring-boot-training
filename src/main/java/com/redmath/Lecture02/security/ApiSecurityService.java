package com.redmath.Lecture02.security;

import com.redmath.Lecture02.user.ApiUser;
import com.redmath.Lecture02.user.ApiUserService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

@Service
public class ApiSecurityService {

    private final NimbusJwtEncoder jwtEncoder;
    private final NimbusJwtDecoder jwtDecoder;

    public ApiSecurityService(ApiUserService userService) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            this.jwtEncoder = NimbusJwtEncoder.withKeyPair(publicKey , privateKey).algorithm(SignatureAlgorithm.PS256).build();
            this.jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey)
                    .signatureAlgorithm(SignatureAlgorithm.PS256)
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to generate RSA key pair", ex);
        }
    }

    public NimbusJwtDecoder jwtDecoder() {
        return jwtDecoder;
    }

    public String generateToken(ApiUser apiUser) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(apiUser.getUserName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(300))
                .claim("scope", apiUser.getRoles().replace(",", " "))
                .claim("jti", java.util.UUID.randomUUID().toString())
                .build();

        return jwtEncoder.encode(
                        JwtEncoderParameters.from(
                                JwsHeader.with(SignatureAlgorithm.PS256).build(),
                                claims))
                .getTokenValue();
    }

}
