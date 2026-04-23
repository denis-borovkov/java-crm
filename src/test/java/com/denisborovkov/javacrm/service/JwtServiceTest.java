package com.denisborovkov.javacrm.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("my-super-secret-key-my-super-secret-value");
        user = User.withUsername("jwt-user@example.com")
                .password("secret")
                .roles("USER")
                .build();
    }

    @Test
    void generateRefreshTokenProducesUniqueTokensForBackToBackCalls() {
        String firstToken = jwtService.generateRefreshToken(user);
        String secondToken = jwtService.generateRefreshToken(user);

        assertNotEquals(firstToken, secondToken);

        Claims firstClaims = jwtService.getClaims(firstToken);
        Claims secondClaims = jwtService.getClaims(secondToken);
        assertEquals("refresh", firstClaims.get("type"));
        assertEquals("refresh", secondClaims.get("type"));
        assertNotEquals(firstClaims.getId(), secondClaims.getId());
        assertTrue(jwtService.isRefreshToken(firstToken));
        assertTrue(jwtService.isRefreshToken(secondToken));
    }
}
