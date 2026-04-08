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
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid SignupRequest request) {
        UserDTO user = authService.registerUser(request);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody @Valid SigninRequest request) {
        return ResponseEntity.ok().body(authService.signinUser(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity.ok().body(tokenService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest request) {
        tokenService.logoutRevokeToken(request);
        return ResponseEntity.ok().body("You have been logged out");
    }

    @PostMapping("/forgot")
    public ResponseEntity<ForgotResponse> forgot(@RequestBody @Valid ForgotRequest request) {
        return ResponseEntity.ok().body(authService.forgotPassword(request));
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
