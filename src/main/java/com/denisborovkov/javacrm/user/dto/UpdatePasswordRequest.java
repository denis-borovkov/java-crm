package com.denisborovkov.javacrm.user.dto;

public record UpdatePasswordRequest(Long id, String oldPassword, String newPassword) {
}



