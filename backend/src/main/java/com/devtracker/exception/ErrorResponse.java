package com.devtracker.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Normalised REST error response payload.
 *
 * This contract perfectly mirrors the frontend ApiError type structure
 * used by the Vue 3 Axios interceptors to process server-side validation 
 * and operational exceptions predictably.
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    Map<String, String> fieldErrors
) {}
