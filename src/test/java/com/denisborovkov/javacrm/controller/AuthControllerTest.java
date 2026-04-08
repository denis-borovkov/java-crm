package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.entity.OTToken;
import com.denisborovkov.javacrm.entity.RefreshToken;
import com.denisborovkov.javacrm.entity.User;
import com.denisborovkov.javacrm.enums.Role;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.repository.CustomerRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockitoBean
    private OneTimeTokenRepository oneTimeTokenRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private CustomerMapper customerMapper;

    private Map<String, User> usersByEmail;
    private Map<String, RefreshToken> refreshTokens;
    private Map<String, OTToken> oneTimeTokens;
    private AtomicLong userIds;

    @BeforeEach
    void setUp() {
        usersByEmail = new LinkedHashMap<>();
        refreshTokens = new LinkedHashMap<>();
        oneTimeTokens = new LinkedHashMap<>();
        userIds = new AtomicLong(1);

        when(userRepository.existsByEmail(anyString()))
                .thenAnswer(invocation -> usersByEmail.containsKey(normalizeEmail(invocation.getArgument(0, String.class))));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0, User.class);
                    if (user.getId() == null) {
                        user.setId(userIds.getAndIncrement());
                    }
                    usersByEmail.put(normalizeEmail(user.getEmail()), user);
                    return user;
                });
        when(userRepository.findUsersByEmail(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(usersByEmail.get(normalizeEmail(invocation.getArgument(0, String.class)))));
        when(userRepository.findUserByEmail(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(usersByEmail.get(normalizeEmail(invocation.getArgument(0, String.class)))));
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocation -> usersByEmail.values().stream()
                        .filter(user -> user.getId().equals(invocation.getArgument(0, Long.class)))
                        .findFirst());
        lenient().when(userRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(usersByEmail.values()));

        when(userMapper.toDTO(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0, User.class);
                    return new UserDTO(user.getEmail(), user.getRole().name());
                });

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> {
                    RefreshToken token = invocation.getArgument(0, RefreshToken.class);
                    refreshTokens.put(token.getToken(), token);
                    return token;
                });
        when(refreshTokenRepository.saveAll(anyIterable()))
                .thenAnswer(invocation -> {
                    Iterable<RefreshToken> tokens = invocation.getArgument(0);
                    List<RefreshToken> saved = new ArrayList<>();
                    for (RefreshToken token : tokens) {
                        refreshTokens.put(token.getToken(), token);
                        saved.add(token);
                    }
                    return saved;
                });
        when(refreshTokenRepository.findByToken(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(refreshTokens.get(invocation.getArgument(0, String.class))));
        when(refreshTokenRepository.findAllByEmail(anyString()))
                .thenAnswer(invocation -> refreshTokens.values().stream()
                        .filter(token -> token.getEmail().equals(normalizeEmail(invocation.getArgument(0, String.class))))
                        .toList());

        when(oneTimeTokenRepository.save(any(OTToken.class)))
                .thenAnswer(invocation -> {
                    OTToken token = invocation.getArgument(0, OTToken.class);
                    oneTimeTokens.put(token.getTokenValue(), token);
                    return token;
                });
        when(oneTimeTokenRepository.findById(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(oneTimeTokens.get(invocation.getArgument(0, String.class))));
        when(oneTimeTokenRepository.findTopByEmailOrderByIssuedAtDesc(anyString()))
                .thenAnswer(invocation -> oneTimeTokens.values().stream()
                        .filter(token -> token.getEmail().equals(normalizeEmail(invocation.getArgument(0, String.class))))
                        .max(Comparator.comparing(OTToken::getIssuedAt)));
        lenient().doAnswer(invocation -> {
            oneTimeTokens.remove(invocation.getArgument(0, OTToken.class).getTokenValue());
            return null;
        }).when(oneTimeTokenRepository).delete(any(OTToken.class));
        lenient().doAnswer(invocation -> {
            String email = normalizeEmail(invocation.getArgument(0, String.class));
            oneTimeTokens.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));
            return null;
        }).when(oneTimeTokenRepository).deleteAllByEmail(anyString());
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

        User storedUser = usersByEmail.get("new-user@example.com");
        assertTrue(passwordEncoder.matches("sup3r-secret", storedUser.getPassword()));
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

        assertFalse(refreshTokens.isEmpty());
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

        JsonNode refreshResponse = objectMapper.readTree(responseBody);
        String newRefreshToken = refreshResponse.get("newRefreshToken").asText();

        assertNotEquals(oldRefreshToken, newRefreshToken);
        assertTrue(refreshTokens.containsKey(newRefreshToken));
        assertFalse(refreshTokens.get(newRefreshToken).isRevoked());
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
                .andExpect(status().isOk())
                .andExpect(content().string("You have been logged out"));

        assertTrue(refreshTokens.get(refreshToken).isRevoked());
    }

    @Test
    void forgotCreatesOneTimeTokenLink() throws Exception {
        mockMvc.perform(post("/api/v1/auth/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "forgot-user@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oneTimeToken").value(org.hamcrest.Matchers.startsWith("http://localhost:3000/")));

        assertFalse(oneTimeTokens.isEmpty());
    }

    @Test
    void resetConsumesOneTimeTokenAndUpdatesPassword() throws Exception {
        User user = seedUser("reset-user@example.com", "old-password");
        oneTimeTokens.put("reset-token", OTToken.builder()
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
                .andExpect(status().isOk())
                .andExpect(content().string("Password has been reset"));

        assertFalse(oneTimeTokens.containsKey("reset-token"));
        assertTrue(passwordEncoder.matches("new-password", usersByEmail.get(user.getEmail()).getPassword()));
    }

    @Test
    void meReturnsAuthenticatedUsername() throws Exception {
        seedUser("me-user@example.com", "me-password");
        JsonNode signinResponse = signin("me-user@example.com", "me-password");
        String accessToken = signinResponse.get("accessToken").asText();

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("me-user@example.com"));
    }

    private User seedUser(String email, String rawPassword) {
        User user = User.builder()
                .email(normalizeEmail(email))
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build();
        usersByEmail.put(user.getEmail(), user);
        user.setId(userIds.getAndIncrement());
        return user;
    }

    private JsonNode signin(String email, String password) throws Exception {
        String responseBody = mockMvc.perform(post("/api/v1/auth/signin")
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

        return objectMapper.readTree(responseBody);
    }

    private String normalizeEmail(String email) {
        return email.toLowerCase(Locale.ENGLISH);
    }
}
