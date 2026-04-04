package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.*;
import com.denisborovkov.javacrm.enums.Role;
import com.denisborovkov.javacrm.mapper.UserMapper;
import com.denisborovkov.javacrm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public SigninResponse signin(SigninRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        assert user != null;
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.createRefreshToken(refreshToken, user);
        return new SigninResponse(accessToken, refreshToken);
    }

    public UserDTO registerUser(SignupRequest request) {
        validateSignup(request);
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .role(Role.USER)
                .build();
        return userMapper.toDTO(userService.createUser(user));
    }

    public UserDTO registerAdmin(CreateAdminRequest request) {
        User admin = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .role(Role.ADMIN)
                .build();
        return userMapper.toDTO(userService.createAdmin(admin));
    }

    private void validateSignup(SignupRequest request) {
        if (userService.existsUserByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userService.existsUserByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
