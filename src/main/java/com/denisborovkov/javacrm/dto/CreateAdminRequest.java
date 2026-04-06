package com.denisborovkov.javacrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdminRequest(String username, String password,@Email @NotBlank String email){
}
