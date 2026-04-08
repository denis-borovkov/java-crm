package com.denisborovkov.javacrm.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(String oneTimeToken, @NotBlank String newPassword) {
}
