package com.crediya.solicitudes.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SortDirection {
    ASC("ASC", "Ascendente"),
    DESC("DESC", "Descendente");

    private final String value;
    private final String description;

    public static boolean isValidDirection(String direction) {
        return Arrays.stream(values())
                .anyMatch(dir -> dir.getValue().equalsIgnoreCase(direction));
    }

    public static SortDirection fromValue(String direction) {
        return Arrays.stream(values())
                .filter(dir -> dir.getValue().equalsIgnoreCase(direction))
                .findFirst()
                .orElse(ASC);
    }

    public static String getValidDirectionsAsString() {
        return Arrays.stream(values())
                .map(SortDirection::getValue)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}