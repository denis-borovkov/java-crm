package com.denisborovkov.javacrm.dto;

public record UpdatePasswordRequest(Long id, String oldPassword, String newPassword) {
}
