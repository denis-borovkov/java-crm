package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.entity.RecoveryToken;
import com.denisborovkov.javacrm.exception.OneTimeTokenRateLimitException;
import com.denisborovkov.javacrm.repository.OneTimeTokenRepository;
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
    private JpaOneTimeTokenService jpaOneTimeTokenService;

    @BeforeEach
    void setUp() {
        this.jpaOneTimeTokenService = new JpaOneTimeTokenService(oneTimeTokenRepository);
    }

    @Test
    void createOneTimeTokenRejectsRequestsInsideCooldown() {
        RecoveryToken latestToken = mock(RecoveryToken.class);
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
        RecoveryToken latestToken = mock(RecoveryToken.class);
        when(latestToken.getIssuedAt()).thenReturn(Instant.now().minus(Duration.ofMinutes(16)));

        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenCooldown", Duration.ofMinutes(15));
        ReflectionTestUtils.setField(jpaOneTimeTokenService, "oneTimeTokenTtl", Duration.ofMinutes(15));
        when(oneTimeTokenRepository.findTopByEmailOrderByIssuedAtDesc("user@example.com"))
                .thenReturn(Optional.of(latestToken));

        String token = jpaOneTimeTokenService.createOneTimeToken("user@example.com");

        assertNotNull(token);
        verify(oneTimeTokenRepository).deleteAllByEmail("user@example.com");
        verify(oneTimeTokenRepository).save(any(RecoveryToken.class));
    }

    @Test
    void useOneTimeTokenRevokesTokenAfterSuccessfulUse() {
        RecoveryToken storedToken = mock(RecoveryToken.class);
        when(storedToken.getTokenValue()).thenReturn("sample-token");
        when(storedToken.getEmail()).thenReturn("user@example.com");
        when(storedToken.getExpiresAt()).thenReturn(Instant.now().plusSeconds(60));
        when(oneTimeTokenRepository.findById("sample-token")).thenReturn(Optional.of(storedToken));

        String email = jpaOneTimeTokenService.useOneTimeToken("sample-token");

        assertEquals("user@example.com", email);
        verify(oneTimeTokenRepository).delete(storedToken);
    }
}
