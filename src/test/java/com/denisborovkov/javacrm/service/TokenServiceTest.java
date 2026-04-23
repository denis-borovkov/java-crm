package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.token.RefreshRequest;
import com.denisborovkov.javacrm.dto.token.RefreshResponse;
import com.denisborovkov.javacrm.entity.RefreshToken;
import com.denisborovkov.javacrm.mapper.RefreshTokenMapper;
import com.denisborovkov.javacrm.dao.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenMapper refreshTokenMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void refreshRotatesRefreshToken() {
        String oldTokenValue = "old-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        UserDetails user = User.withUsername("alice@mail.ru")
                .password("secret")
                .authorities("ROLE_USER")
                .build();

        RefreshToken storedToken = new RefreshToken();
        storedToken.setToken(oldTokenValue);
        storedToken.setEmail(user.getUsername());
        storedToken.setRevoked(false);
        storedToken.setExpiryDate(Instant.now().plusSeconds(3600));

        when(refreshTokenRepository.findByToken(oldTokenValue)).thenReturn(Optional.of(storedToken));
        when(jwtService.isRefreshToken(oldTokenValue)).thenReturn(true);
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn(newAccessToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(newRefreshToken);
        when(refreshTokenMapper.toEntity(any(), any(), any()))
                .thenAnswer(invocation -> RefreshToken.builder()
                        .token(invocation.getArgument(0, String.class))
                        .email(invocation.getArgument(1, String.class))
                        .expiryDate(invocation.getArgument(2, Instant.class))
                        .revoked(false)
                        .build());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshResponse response = tokenService.refreshToken(new RefreshRequest(oldTokenValue));

        assertEquals(newAccessToken, response.newAccessToken());
        assertEquals(newRefreshToken, response.newRefreshToken());
        assertTrue(storedToken.isRevoked());

        ArgumentCaptor<RefreshToken> savedTokens = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(2)).save(savedTokens.capture());

        RefreshToken rotatedToken = savedTokens.getAllValues().get(1);
        assertEquals(user.getUsername(), rotatedToken.getEmail());
        assertEquals(newRefreshToken, rotatedToken.getToken());
        assertFalse(rotatedToken.isRevoked());
        assertTrue(rotatedToken.getExpiryDate().isAfter(Instant.now().plusSeconds(6 * 24 * 60 * 60)));
    }

    @Test
    void refreshRejectsRevokedToken() {
        RefreshToken storedToken = new RefreshToken();
        storedToken.setToken("revoked-token");
        storedToken.setEmail("alice@mail.ru");
        storedToken.setRevoked(true);
        storedToken.setExpiryDate(Instant.now().plusSeconds(3600));

        when(refreshTokenRepository.findByToken("revoked-token")).thenReturn(Optional.of(storedToken));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tokenService.refreshToken(new RefreshRequest("revoked-token")));

        assertEquals("Token is already revoked", exception.getMessage());
    }

    @Test
    void refreshRejectsExpiredToken() {
        RefreshToken storedToken = new RefreshToken();
        storedToken.setToken("expired-token");
        storedToken.setEmail("alice@mail.ru");
        storedToken.setRevoked(false);
        storedToken.setExpiryDate(Instant.now().minusSeconds(1));

        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(storedToken));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tokenService.refreshToken(new RefreshRequest("expired-token")));

        assertEquals("Token is expired", exception.getMessage());
    }

    @Test
    void refreshRejectsNonRefreshToken() {
        RefreshToken storedToken = new RefreshToken();
        storedToken.setToken("access-token");
        storedToken.setEmail("alice@mail.ru");
        storedToken.setRevoked(false);
        storedToken.setExpiryDate(Instant.now().plusSeconds(3600));

        when(refreshTokenRepository.findByToken("access-token")).thenReturn(Optional.of(storedToken));
        when(jwtService.isRefreshToken("access-token")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tokenService.refreshToken(new RefreshRequest("access-token")));

        assertEquals("Token is not refreshed", exception.getMessage());
    }
}
