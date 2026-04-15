package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.entity.OTToken;
import com.denisborovkov.javacrm.entity.RefreshToken;
import com.denisborovkov.javacrm.entity.UserEntity;
import com.denisborovkov.javacrm.enums.Role;
import com.denisborovkov.javacrm.repository.OneTimeTokenRepository;
import com.denisborovkov.javacrm.repository.RefreshTokenRepository;
import com.denisborovkov.javacrm.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private OneTimeTokenRepository oneTimeTokenRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        oneTimeTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void signupCreatesUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "new-user@example.com",
                                  "password": "sup3r-secret"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new-user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        UserEntity stored = userRepository.findUserByEmail("new-user@example.com").orElseThrow();
        assertTrue(passwordEncoder.matches("sup3r-secret", stored.getPassword()));
    }

    @Test
    void signinReturnsAccessAndRefreshTokens() throws Exception {
        seedUser("signin-user@example.com", "sign-in-password");

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "signin-user@example.com",
                                  "password": "sign-in-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());

        assertEquals(1, refreshTokenRepository.count());
    }

    @Test
    void refreshRotatesTokens() throws Exception {
        seedUser("refresh-user@example.com", "refresh-password");
        JsonNode signinResponse = signin("refresh-user@example.com", "refresh-password");
        String oldRefreshToken = signinResponse.get("refreshToken").asText();

        String responseBody = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(oldRefreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newAccessToken").isString())
                .andExpect(jsonPath("$.newRefreshToken").isString())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String newRefreshToken = objectMapper.readTree(responseBody).get("newRefreshToken").asText();

        assertNotEquals(oldRefreshToken, newRefreshToken);
        assertTrue(refreshTokenRepository.findByToken(oldRefreshToken).orElseThrow().isRevoked());
        assertFalse(refreshTokenRepository.findByToken(newRefreshToken).orElseThrow().isRevoked());
    }

    @Test
    void refreshRejectsReuseOfRevokedToken() throws Exception {
        seedUser("reuse-user@example.com", "reuse-password");
        JsonNode signinResponse = signin("reuse-user@example.com", "reuse-password");
        String refreshToken = signinResponse.get("refreshToken").asText();

        // first refresh — consumes the token
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "refreshToken": "%s" }
                                """.formatted(refreshToken)))
                .andExpect(status().isOk());

        // second refresh with the same token — must be rejected
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "refreshToken": "%s" }
                                """.formatted(refreshToken)))
                .andExpect(status().isUnauthorized());

        // reuse detection must revoke all tokens for this user
        assertTrue(refreshTokenRepository
                .findAllByEmail("reuse-user@example.com")
                .stream()
                .allMatch(RefreshToken::isRevoked));
    }

    @Test
    void logoutRevokesRefreshToken() throws Exception {
        seedUser("logout-user@example.com", "logout-password");
        JsonNode signinResponse = signin("logout-user@example.com", "logout-password");
        String refreshToken = signinResponse.get("refreshToken").asText();

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(refreshToken)))
                .andExpect(status().isOk());

        assertTrue(refreshTokenRepository.findByToken(refreshToken).orElseThrow().isRevoked());
    }

    @Test
    void forgotCreatesOneTimeToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "forgot-user@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oneTimeToken").isString());

        assertEquals(1, oneTimeTokenRepository.count());
    }

    @Test
    void resetConsumesOneTimeTokenAndUpdatesPassword() throws Exception {
        UserEntity user = seedUser("reset-user@example.com", "old-password");
        oneTimeTokenRepository.save(OTToken.builder()
                .tokenValue("reset-token")
                .email(user.getEmail())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build());

        mockMvc.perform(post("/api/v1/auth/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oneTimeToken": "reset-token",
                                  "newPassword": "new-password"
                                }
                                """))
                .andExpect(status().isOk());

        assertFalse(oneTimeTokenRepository.existsById("reset-token"));
        UserEntity updated = userRepository.findUserByEmail(user.getEmail()).orElseThrow();
        assertTrue(passwordEncoder.matches("new-password", updated.getPassword()));
    }

    @Test
    void meReturnsAuthenticatedUsername() throws Exception {
        seedUser("me-user@example.com", "me-password");
        String accessToken = signin("me-user@example.com", "me-password")
                .get("accessToken").asText();

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("me-user@example.com"));
    }

    // --- helpers ---

    private UserEntity seedUser(String email, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setEmail(email.toLowerCase(Locale.ENGLISH));
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    private JsonNode signin(String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(body);
    }
}