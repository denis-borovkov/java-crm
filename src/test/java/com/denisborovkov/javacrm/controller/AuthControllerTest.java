package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.ForgotRequest;
import com.denisborovkov.javacrm.dto.ForgotResponse;
import com.denisborovkov.javacrm.dto.ResetPasswordRequest;
import com.denisborovkov.javacrm.security.JwtAuthFilter;
import com.denisborovkov.javacrm.service.AuthService;
import com.denisborovkov.javacrm.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void forgotReturnsRecoveryToken() throws Exception {
        ForgotResponse response = new ForgotResponse("recovery-jwt");
        when(authService.forgot(any(ForgotRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ForgotRequest("user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recoveryToken").value("recovery-jwt"));

        verify(authService).forgot(any(ForgotRequest.class));
    }

    @Test
    void resetReturnsSuccessMessage() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("recovery-jwt", "new-password");
        doNothing().when(authService).resetPassword(any(ResetPasswordRequest.class));

        mockMvc.perform(post("/api/v1/auth/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password has been reset"));

        verify(authService).resetPassword(any(ResetPasswordRequest.class));
    }
}
