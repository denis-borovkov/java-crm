package com.denisborovkov.utils;

import com.denisborovkov.interfaces.UserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "SuperSecretKeyForJwtGeneration228";
    private static final int EXPIRATION_TIME = 6000000;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    public String validateToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}