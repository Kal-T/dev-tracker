package com.devtracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * (1) MethodArgumentNotValidException: Handles validation failures on incoming requests.
     * Extracts field names and messages into the fieldErrors map for frontend validation mapping (e.g., VeeValidate).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Validation failed for request: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "One or more input fields failed validation constraints.",
                fieldErrors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * (2) ResourceNotFoundException: Catches custom look-up failures for missing records.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource lookup failed: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * (3) AccessDeniedException: Handles security authorization checks failures.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Security Access Denied: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You do not have permission to access this resource.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * (4) DataIntegrityViolationException: Catches database constraint violations.
     * Identifies duplicate emails during user registration and returns a clean HTTP 409 status.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Database integrity violation: {}", ex.getMessage());
        
        String message = "A data integrity violation occurred.";
        String errorType = "Conflict";
        HttpStatus status = HttpStatus.CONFLICT;

        // Custom validation parsing for registration email constraint failures
        if (ex.getMessage() != null && (ex.getMessage().contains("users") || ex.getMessage().contains("email"))) {
            message = "An account with this email address already exists.";
        }

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorType,
                message,
                null
        );
        return new ResponseEntity<>(response, status);
    }

    /**
     * (5) Generic Exception Fallback: Catches all unmapped runtime exceptions.
     * Prevents technical stack traces from leaking to the outside world.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected runtime exception captured by fallback handler", ex);
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected server error occurred. Please contact administrative support.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
