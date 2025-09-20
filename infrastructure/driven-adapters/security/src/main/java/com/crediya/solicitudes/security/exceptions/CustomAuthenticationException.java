package com.crediya.solicitudes.security.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CustomAuthenticationException extends IllegalArgumentException {

    private final String code = "AUTHENTICATION_ERROR";

    public CustomAuthenticationException(String message) {
        super(message);
        log.error("{}: {}", code, message);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", code, getMessage());
    }
}
