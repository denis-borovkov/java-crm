package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return authService.registerAdmin(request);
    }
}
