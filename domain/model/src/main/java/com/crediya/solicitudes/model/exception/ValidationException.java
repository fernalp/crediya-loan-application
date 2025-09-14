package com.crediya.solicitudes.model.exception;

import lombok.Getter;

@Getter
public class ValidationException extends IllegalArgumentException{
    private final String code = "VALIDATION_ERROR";

    public ValidationException(String message){
        super(message);
    }

}
