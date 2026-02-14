package com.example.hms.security;

import com.example.hms.entity.User;
import com.example.hms.entity.type.Authprovidertype;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class Authutil {
    @Value("${jwt.secretkey}")
 private String jwtSecret;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String token){

        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token)
                .getPayload().getSubject();
//        return claims.getSubject();
    }

    public Authprovidertype getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> Authprovidertype.GOOGLE;
            case "github" -> Authprovidertype.GITHUB;
            case "facebook" -> Authprovidertype.FACEBOOK;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        };
    }


}
