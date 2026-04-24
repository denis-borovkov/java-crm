package com.denisborovkov.javacrm.auth.service;

import com.denisborovkov.javacrm.auth.dto.*;
import com.denisborovkov.javacrm.user.dto.UserDTO;
import com.denisborovkov.javacrm.auth.token.ForgotRequest;
import com.denisborovkov.javacrm.auth.token.ForgotResponse;
import com.denisborovkov.javacrm.user.domain.UserEntity;
import com.denisborovkov.javacrm.user.mapper.UserMapper;
import com.denisborovkov.javacrm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final JpaOneTimeTokenService jpaOneTimeTokenService;

    public SigninResponse signinUser(SigninRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return tokenService.issueTokens(user);
    }

    public UserDTO registerUser(SignupRequest request) {
        validateSignup(request);
        UserEntity userEntity = userService.createUser(request);
        return userMapper.toDTO(userEntity);
    }

    public UserDTO registerAdmin(CreateAdminRequest request) {
        UserEntity admin = userService.createAdmin(request);
        return userMapper.toDTO(admin);
    }

    public ForgotResponse forgotPassword(ForgotRequest request) {
        String oneTimeToken = jpaOneTimeTokenService.createOneTimeToken(request.email());
        return new ForgotResponse(oneTimeToken);
    }

    public void resetPassword(ResetPasswordRequest request) {
        String email = jpaOneTimeTokenService.useOneTimeToken(request.oneTimeToken());
        userService.updatePassword(email, request.newPassword());
    }

    private void validateSignup(SignupRequest request) {
        if (userService.existsUserByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}



