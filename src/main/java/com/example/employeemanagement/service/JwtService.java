package com.example.employeemanagement.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long expirationSeconds;

    public JwtService(
            JwtEncoder jwtEncoder) {

        this.jwtEncoder =
                jwtEncoder;
    }

    public String generateToken(
            Authentication authentication) {

        Instant now =
                Instant.now();

        String role =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(
                                GrantedAuthority::getAuthority
                        )
                        .findFirst()
                        .orElse("ROLE_USER");

        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer(
                                "employee-management"
                        )
                        .issuedAt(now)
                        .expiresAt(
                                now.plus(
                                        expirationSeconds,
                                        ChronoUnit.SECONDS
                                )
                        )
                        .subject(
                                authentication.getName()
                        )
                        .claim(
                                "role",
                                role
                        )
                        .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                claims
                        )
                )
                .getTokenValue();
    }
}