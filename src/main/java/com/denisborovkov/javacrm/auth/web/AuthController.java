package com.denisborovkov.javacrm.auth.web;

import com.denisborovkov.javacrm.auth.dto.*;
import com.denisborovkov.javacrm.user.dto.UserDTO;
import com.denisborovkov.javacrm.auth.token.ForgotRequest;
import com.denisborovkov.javacrm.auth.token.ForgotResponse;
import com.denisborovkov.javacrm.auth.token.RefreshRequest;
import com.denisborovkov.javacrm.auth.token.RefreshResponse;
import com.denisborovkov.javacrm.auth.service.AuthService;
import com.denisborovkov.javacrm.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return authService.signinUser(request);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestBody @Valid RefreshRequest request) {
        return tokenService.refreshToken(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshRequest request) {
        tokenService.logoutRevokeToken(request);
    }

    @PostMapping("/forgot")
    public ForgotResponse forgot(@RequestBody @Valid ForgotRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset")
    public void reset(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
    }

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName();
    }
}



