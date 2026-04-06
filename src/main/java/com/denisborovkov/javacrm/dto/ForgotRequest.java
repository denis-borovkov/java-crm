package com.denisborovkov.javacrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotRequest(@Email @NotBlank String email) {
}
