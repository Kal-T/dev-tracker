package com.devtracker.dto.response;

import java.util.List;

public record PaginatedResponse<T>(
    List<T> data,
    long total,
    int page,
    int totalPages
) {}
