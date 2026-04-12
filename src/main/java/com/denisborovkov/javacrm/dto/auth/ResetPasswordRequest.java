package com.denisborovkov.javacrm.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(String oneTimeToken, @NotBlank String newPassword) {
}
