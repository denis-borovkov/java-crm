package com.denisborovkov.javacrm.dto.entity;

public record UpdatePasswordRequest(Long id, String oldPassword, String newPassword) {
}
