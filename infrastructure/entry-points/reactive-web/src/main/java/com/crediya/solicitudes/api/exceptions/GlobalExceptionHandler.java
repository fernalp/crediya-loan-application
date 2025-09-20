package com.crediya.solicitudes.api.exceptions;

import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.security.exceptions.CustomAuthenticationException;
import com.crediya.solicitudes.usecase.exceptions.CommunicationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties.Resources resources,
            ApplicationContext applicationContext,
            ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        Throwable error = getError(serverRequest);
        Map<String, Object> errorAttributes = this.getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        if (error instanceof ValidationException || error instanceof ServerWebInputException){
            return renderBadRequestException(errorAttributes);
        }
        if (error instanceof CommunicationException){
            return badGatewayException(errorAttributes);
        }
        if (error instanceof CustomAuthenticationException){
            return renderUnauthorizedException(errorAttributes);
        }
        return renderInternalServerError(errorAttributes);
    }

    private Mono<ServerResponse> renderInternalServerError(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> renderConflictException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> renderNotFoundException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> renderUnauthorizedException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> renderForbiddenException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> renderBadRequestException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> badGatewayException(Map<String, Object> errorAttributes) {
        return ServerResponse
                .status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }
}
