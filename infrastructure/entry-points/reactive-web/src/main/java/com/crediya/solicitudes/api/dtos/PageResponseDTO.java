package com.crediya.solicitudes.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PageResponseDTO<T>(
        @JsonProperty("pagina")
        int page,
        @JsonProperty("cantidad")
        int size,
        @JsonProperty("total_elementos")
        long totalElements,
        @JsonProperty("total_paginas")
        int totalPages,
        @JsonProperty("items")
        List<T> items
) {}
