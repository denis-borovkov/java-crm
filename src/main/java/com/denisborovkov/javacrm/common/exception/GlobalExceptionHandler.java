package com.denisborovkov.javacrm.common.exception;

import com.denisborovkov.javacrm.common.exception.ErrorResponse;
import com.denisborovkov.javacrm.auth.exception.*;
import com.denisborovkov.javacrm.customer.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CustomerNotFoundException.class,
            RefreshTokenNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return build(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            RefreshTokenIsAlreadyRevokedException.class,
            RefreshTokenIsExpiredException.class,
            RefreshTokenIsRequiredException.class,
            BadCredentialsException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        return build(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({
            PasswordMismatchException.class,
            RefreshTokenIsNotRefreshedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return build(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(OneTimeTokenRateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimit(OneTimeTokenRateLimitException ex, HttpServletRequest request) {
        return build(ex, HttpStatus.TOO_MANY_REQUESTS, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return build(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(message, ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return build(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> build(Exception ex, HttpStatus status, HttpServletRequest request) {
        return build(ex.getMessage(), ex.getClass().getSimpleName(), status, request);
    }

    private ResponseEntity<ErrorResponse> build(String message, String errorCode, HttpStatus status, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                message,
                errorCode,
                status.value(),
                Instant.now().toEpochMilli(),
                request.getRequestURI()
        ));
    }
}



