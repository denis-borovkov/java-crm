package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.RefreshRequest;
import com.denisborovkov.javacrm.dto.RefreshResponse;
import com.denisborovkov.javacrm.dto.SigninResponse;
import com.denisborovkov.javacrm.entity.RefreshToken;
import com.denisborovkov.javacrm.mapper.RefreshTokenMapper;
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
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public SigninResponse issueTokens(UserDetails user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        createRefreshToken(refreshToken, user);
        return new SigninResponse(accessToken, refreshToken);
    }

    public void createRefreshToken(String token, UserDetails user) {
        RefreshToken refreshToken = refreshTokenMapper.toEntity(
                token,
                user.getUsername(),
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshResponse refreshToken(RefreshRequest request) {
        String oldToken = normalizeToken(request.refreshToken());
        RefreshToken stored = refreshTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        validateRefreshToken(stored, oldToken);
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        UserDetails user = userDetailsService.loadUserByUsername(stored.getEmail());
        SigninResponse tokens = issueTokens(user);
        return new RefreshResponse(tokens.accessToken(), tokens.refreshToken());
    }

    public void logoutRevokeToken(RefreshRequest request) {
        refreshTokenRepository.findByToken(normalizeToken(request.refreshToken()))
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public void revokeAllUserTokens(String username) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByEmail(username);
        for (RefreshToken token : tokens) {
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(tokens);
    }

    public void validateRefreshToken(RefreshToken stored, String oldToken) {
        if (stored.isRevoked()) {
            revokeAllUserTokens(stored.getEmail());
            throw  new RuntimeException("Token is already revoked");
        }
        if (stored.getExpiryDate().isBefore(Instant.now())) {
            throw  new RuntimeException("Token is expired");
        }
        if (!jwtService.isRefreshToken(oldToken)) {
            throw  new RuntimeException("Token is not refreshed");
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
