package com.example.hms.security;

import com.example.hms.entity.User;
import com.example.hms.entity.type.Authprovidertype;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

   public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User,String registrationId){
        String providerId= switch (registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();

            default -> {
                throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
            }
        };

       if (providerId == null || providerId.isBlank()) {

           throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
       }
       return providerId;
   }
    public String determineUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId) {
        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            return email;
        }
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }
}
