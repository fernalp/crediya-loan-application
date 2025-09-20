package com.crediya.solicitudes.model;

import java.util.List;

public record PageResponse<T>(
        int page,
        int size,
        int totalElements,
        int totalPages,
        List<T> content
) {
}
