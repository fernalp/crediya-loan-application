package com.crediya.solicitudes.model;

import java.util.List;

public record PageResponse<T>(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        List<T> content
) {
}
