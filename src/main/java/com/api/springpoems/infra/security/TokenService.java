package com.api.springpoems.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.springpoems.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private String issuer = "Spring-Poems API.com";

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret); 
            return JWT.create()
                .withIssuer(issuer) 
                .withSubject(user.getUsername()) 
                .withExpiresAt(expirationDate()) 
                .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error on generating token jwt", exception);
        } 
    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(tokenJWT)
                .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired token JWT!");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}