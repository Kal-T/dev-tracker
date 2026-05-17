package com.devtracker.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    String status,
    String priority,
    List<String> tags,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String ownerEmail
) {}
