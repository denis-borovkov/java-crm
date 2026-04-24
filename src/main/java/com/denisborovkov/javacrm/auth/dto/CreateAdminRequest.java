package com.denisborovkov.javacrm.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(@Email @NotBlank String email, @NotBlank String password){
}



