package com.api.springpoems.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class HandlingErros {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlingError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlingError400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ValidationErrorData::new).toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handlingError400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handlingErrorBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handlingErrorAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handlingErrorAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlingError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
    }

    private record ValidationErrorData(String campo, String message) {
        public ValidationErrorData(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handlingErrorBusinessRules(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
