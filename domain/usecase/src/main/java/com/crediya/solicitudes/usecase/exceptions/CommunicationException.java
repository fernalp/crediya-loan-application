package com.crediya.solicitudes.usecase.exceptions;

import lombok.Getter;

@Getter
public class CommunicationException extends RuntimeException {

    private final String code = "COMMUNICATION_ERROR";

    public CommunicationException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", code, super.getMessage());
    }

}
