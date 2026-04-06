package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.*;
import com.denisborovkov.javacrm.service.AuthService;
import com.denisborovkov.javacrm.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public UserDTO signup(@RequestBody @Valid SignupRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/signin")
    public SigninResponse signin(@RequestBody @Valid SigninRequest request) {
        return authService.signin(request);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestBody @Valid RefreshRequest request) {
        return tokenService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {
        tokenService.logoutRevokeToken(request);
        return ResponseEntity.ok().body("You have been logged out");
    }

    @PostMapping("/forgot")
    public ForgotResponse forgot(@RequestBody @Valid ForgotRequest request) {
        return authService.forgot(request);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().body("Password has been reset");
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName());
    }
}
