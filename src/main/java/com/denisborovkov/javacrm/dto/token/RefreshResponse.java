package com.denisborovkov.javacrm.dto.token;

public record RefreshResponse(String newAccessToken, String newRefreshToken) {
}
