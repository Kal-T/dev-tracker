package com.devtracker.dto.request;

import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateTaskRequest(
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters if updated")
    String title,

    @Size(max = 500, message = "Description must not exceed 500 characters if updated")
    String description,

    String status,
    String priority,
    List<String> tags
) {}
