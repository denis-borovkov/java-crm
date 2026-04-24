package com.denisborovkov.javacrm.auth.service;

import com.denisborovkov.javacrm.auth.token.OTToken;
import com.denisborovkov.javacrm.auth.exception.OneTimeTokenRateLimitException;
import com.denisborovkov.javacrm.auth.token.OneTimeTokenMapper;
import com.denisborovkov.javacrm.auth.token.OneTimeTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaOneTimeTokenServiceTest {

    @Mock
    private OneTimeTokenRepository oneTimeTokenRepository;
    @Mock
    private OneTimeTokenMapper oneTimeTokenMapper;
    private JpaOneTimeTokenService jpaOneTimeTokenService;

    @BeforeEach
    void setUp() {
        this.jpaOneTimeTokenService = new JpaOneTimeTokenService(oneTimeTokenRepository, oneTimeTokenMapper);
    }

    @Test
    void createOneTimeTokenRejectsRequestsInsideCooldown() {
        OTToken latestToken = mock(OTToken.class);
        when(latestToken.getIssuedAt()).thenReturn(Instant.now().minus(Duration.ofMinutes(1)));

        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenCooldown", Duration.ofMinutes(15));
        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenTtl", Duration.ofMinutes(15));
        when(oneTimeTokenRepository.findTopByEmailOrderByIssuedAtDesc("user@example.com"))
                .thenReturn(Optional.of(latestToken));

        OneTimeTokenRateLimitException exception = assertThrows(OneTimeTokenRateLimitException.class,
                () -> jpaOneTimeTokenService.createOneTimeToken("user@example.com"));

        assertEquals("One-Time token was issued too recently. Try again later.", exception.getMessage());
    }

    @Test
    void createOneTimeTokenIssuesTokenAfterCooldown() {
        OTToken latestToken = mock(OTToken.class);
        when(latestToken.getIssuedAt()).thenReturn(Instant.now().minus(Duration.ofMinutes(16)));

        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenCooldown", Duration.ofMinutes(15));
        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenTtl", Duration.ofMinutes(15));
        when(oneTimeTokenRepository.findTopByEmailOrderByIssuedAtDesc("user@example.com"))
                .thenReturn(Optional.of(latestToken));
        when(oneTimeTokenMapper.toEntity(any(), any(), any(), any()))
                .thenAnswer(invocation -> OTToken.builder()
                        .tokenValue(invocation.getArgument(0, String.class))
                        .email(invocation.getArgument(1, String.class))
                        .issuedAt(invocation.getArgument(2, Instant.class))
                        .expiresAt(invocation.getArgument(3, Instant.class))
                        .build());

        String token = jpaOneTimeTokenService.createOneTimeToken("user@example.com");

        assertNotNull(token);
        verify(oneTimeTokenRepository).deleteAllByEmail("user@example.com");
        verify(oneTimeTokenRepository).save(any(OTToken.class));
    }

    @Test
    void useOneTimeTokenRevokesTokenAfterSuccessfulUse() {
        OTToken storedToken = mock(OTToken.class);
        when(storedToken.getTokenValue()).thenReturn("sample-token");
        when(storedToken.getEmail()).thenReturn("user@example.com");
        when(storedToken.getExpiresAt()).thenReturn(Instant.now().plusSeconds(60));
        when(oneTimeTokenRepository.findById("sample-token")).thenReturn(Optional.of(storedToken));

        String email = jpaOneTimeTokenService.useOneTimeToken("sample-token");

        assertEquals("user@example.com", email);
        verify(oneTimeTokenRepository).delete(storedToken);
    }
}



