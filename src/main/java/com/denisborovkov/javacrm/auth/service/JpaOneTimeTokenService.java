package com.denisborovkov.javacrm.auth.service;

import com.denisborovkov.javacrm.auth.token.OTToken;
import com.denisborovkov.javacrm.auth.exception.OneTimeTokenRateLimitException;
import com.denisborovkov.javacrm.auth.token.OneTimeTokenMapper;
import com.denisborovkov.javacrm.auth.token.OneTimeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ott.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JpaOneTimeTokenService implements OneTimeTokenService {

    private final OneTimeTokenRepository oneTimeTokenRepository;
    private final OneTimeTokenMapper oneTimeTokenMapper;
    @Value("${app.security.recovery.cooldown:PT1M}")
    private Duration oneTimeTokenCooldown;
    @Value("${app.security.recovery.ttl:PT15M}")
    private Duration oneTimeTokenTtl;

    @Override
    @NullMarked
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        Instant issuedAt = Instant.now();
        OTToken token = oneTimeTokenMapper.toEntity(
                UUID.randomUUID().toString(),
                request.getUsername(),
                issuedAt,
                issuedAt.plus(request.getExpiresIn())
        );
        oneTimeTokenRepository.save(token);
        return new DefaultOneTimeToken(token.getTokenValue(), token.getEmail(), token.getExpiresAt());
    }

    @Override
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        assert authenticationToken.getTokenValue() != null;
        return oneTimeTokenRepository.findById(authenticationToken.getTokenValue())
                .map(token -> {
                    oneTimeTokenRepository.delete(token);
                    if (Instant.now().isAfter(token.getExpiresAt())) {
                        return null;
                    }
                    return new DefaultOneTimeToken(token.getTokenValue(), token.getEmail(), token.getExpiresAt());
                })
                .orElse(null);
    }

    public String useOneTimeToken(String rawToken) {
        OneTimeToken oneTimeToken = consume(new OneTimeTokenAuthenticationToken(rawToken));
        if (oneTimeToken == null) {
            throw new RuntimeException("One-time token is invalid or expired");
        }
        return oneTimeToken.getUsername();
    }

    public String createOneTimeToken(String email) {
        ensureOneTimeTokenCooldown(email);
        revokeOneTimeTokens(email);
        return generate(new GenerateOneTimeTokenRequest(email, oneTimeTokenTtl)).getTokenValue();
    }

    public void revokeOneTimeTokens(String email) {
        oneTimeTokenRepository.deleteAllByEmail(email);
    }

    public void ensureOneTimeTokenCooldown(String email) {
        oneTimeTokenRepository.findTopByEmailOrderByIssuedAtDesc(email)
                .map(OTToken::getIssuedAt)
                .ifPresent(issuedAt -> {
                    Instant nextAllowedIssueTime = issuedAt.plus(oneTimeTokenCooldown);
                    if (nextAllowedIssueTime.isAfter(Instant.now())) {
                        throw new OneTimeTokenRateLimitException("One-Time token was issued too recently. Try again later.");
                    }
                });
    }
}



