package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.RefreshRequest;
import com.denisborovkov.javacrm.dto.RefreshResponse;
import com.denisborovkov.javacrm.dto.SigninResponse;
import com.denisborovkov.javacrm.entity.RecoveryToken;
import com.denisborovkov.javacrm.entity.RefreshToken;
import com.denisborovkov.javacrm.repository.RecoveryTokenRepository;
import com.denisborovkov.javacrm.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RecoveryTokenRepository recoveryTokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public SigninResponse issueTokens(UserDetails user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        createRefreshToken(refreshToken, user);
        return new SigninResponse(accessToken, refreshToken);
    }

    public void createRefreshToken(String token, UserDetails user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUsername(user.getUsername());
        refreshToken.setRevoked(false);
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken);
    }

    public String createRecoveryToken(String email) {
        revokeRecoveryTokens(email);
        String token = jwtService.generateRecoveryToken(email);
        RecoveryToken recoveryToken = new RecoveryToken();
        recoveryToken.setToken(token);
        recoveryToken.setEmail(email);
        recoveryToken.setRevoked(false);
        recoveryToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        recoveryTokenRepository.save(recoveryToken);
        return token;
    }

    public RefreshResponse refreshToken(RefreshRequest request) {
        String oldToken = normalizeToken(request.refreshToken());
        RefreshToken stored = refreshTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        validateRefreshToken(stored, oldToken);
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        UserDetails user = userDetailsService.loadUserByUsername(stored.getUsername());
        SigninResponse tokens = issueTokens(user);
        return new RefreshResponse(tokens.accessToken(), tokens.refreshToken());
    }

    public String useRecoveryToken(String rawToken) {
        String normalizedToken = normalizeToken(rawToken);
        RecoveryToken recoveryToken = recoveryTokenRepository.findByToken(normalizedToken)
                .orElseThrow(() -> new RuntimeException("Recovery token not found"));

        validateRecoveryToken(recoveryToken, normalizedToken);
        recoveryToken.setRevoked(true);
        recoveryTokenRepository.save(recoveryToken);
        return recoveryToken.getEmail();
    }

    public void logoutRevokeToken(RefreshRequest request) {
        refreshTokenRepository.findByToken(normalizeToken(request.refreshToken()))
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public void revokeAllUserTokens(String username) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUsername(username);
        for (RefreshToken token : tokens) {
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(tokens);
    }

    public void revokeRecoveryTokens(String email) {
        List<RecoveryToken> tokens = recoveryTokenRepository.findAllByEmail(email);
        for (RecoveryToken token : tokens) {
            token.setRevoked(true);
        }
        recoveryTokenRepository.saveAll(tokens);
    }

    public void validateRefreshToken(RefreshToken stored, String oldToken) {
        if (stored.isRevoked()) {
            revokeAllUserTokens(stored.getUsername());
            throw  new RuntimeException("Token is already revoked");
        }
        if (stored.getExpiryDate().isBefore(Instant.now())) {
            throw  new RuntimeException("Token is expired");
        }
        if (!jwtService.isRefreshToken(oldToken)) {
            throw  new RuntimeException("Token is not refreshed");
        }
    }

    private void validateRecoveryToken(RecoveryToken recoveryToken, String rawToken) {
        if (recoveryToken.isRevoked()) {
            throw new RuntimeException("Recovery token is already used");
        }
        if (recoveryToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Recovery token is expired");
        }
        if (!jwtService.isRecoveryToken(rawToken)) {
            throw new RuntimeException("Token is not a recovery token");
        }
        String email = jwtService.extractUsername(rawToken);
        if (!recoveryToken.getEmail().equals(email)) {
            throw new RuntimeException("Recovery token does not match user");
        }
    }

    public String normalizeToken(String token) {
        if (token == null) {
            throw new RuntimeException("Refresh token is required");
        }
        String normalized = token.trim();
        if (normalized.startsWith("Bearer ")) {
            normalized = normalized.substring(7).trim();
        }
        return normalized;
    }
}
