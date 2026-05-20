package com.devtracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record BulkStatusUpdateRequest(
    @NotEmpty(message = "Task IDs list must not be empty")
    List<UUID> ids,

    @NotBlank(message = "Status is required")
    String status
) {}
