package com.crediya.solicitudes.api.exceptions;

import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.usecase.exceptions.CommunicationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final String CODE_WEB_INPUT = "VALIDATION_ERROR";
    private static final String MESSAGE_WEB_INPUT = "Datos inválidos";
    private static final String CODE_UNEXPECTED = "ERROR_UNEXPECTED";
    private static final String MESSAGE_UNEXPECTED = "Ocurrió un error inesperado, por favor contacte al administrador";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<>();
        Throwable error = getError(request);

        switch (error) {
            case ValidationException exception -> {
                errorMap.put("code", exception.getCode());
                errorMap.put("message", error.getMessage());
                errorMap.put("timestamp", LocalDateTime.now());
            }
            case ServerWebInputException exception -> {
                errorMap.put("code", CODE_WEB_INPUT);
                errorMap.put("message", MESSAGE_WEB_INPUT);
                errorMap.put("timestamp", LocalDateTime.now());
            }
            case CommunicationException exception -> {
                errorMap.put("code", exception.getCode());
                errorMap.put("message", error.getMessage());
                errorMap.put("timestamp", LocalDateTime.now());
            }
            case null, default -> {
                errorMap.put("code", CODE_UNEXPECTED);
                errorMap.put("message", MESSAGE_UNEXPECTED);
                errorMap.put("timestamp", LocalDateTime.now());
            }
        }

        return errorMap;
    }
}
