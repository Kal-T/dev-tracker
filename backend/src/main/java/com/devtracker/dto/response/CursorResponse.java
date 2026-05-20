package com.devtracker.dto.response;

import java.util.List;

/**
 * Generic response payload for cursor-based pagination.
 *
 * Exposes a flat data page alongside nextCursor representing the 
 * unique ID of the last item in the list, matching the parameter shape 
 * expected by Vue Query's useInfiniteQuery getNextPageParam.
 */
public record CursorResponse<T>(
    List<T> data,
    String nextCursor
) {}
