package com.crediya.solicitudes.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedException implements ServerAccessDeniedHandler {

    private static final String CODE = "FORBIDDEN";
    private static final String MESSAGE_FORBIDDEN = "No tienes los permisos necesarios para acceder a este recurso!!";

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> data = new HashMap<>();
        data.put("code", CODE);
        data.put("message", MESSAGE_FORBIDDEN);
        data.put("timestamp", new Date());

        log.error("{}: {}", CODE, data);

        byte[] responseBody = objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody)));
    }
}
