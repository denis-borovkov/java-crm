package com.denisborovkov.javacrm.auth.token;

public record RefreshResponse(String newAccessToken, String newRefreshToken) {
}



