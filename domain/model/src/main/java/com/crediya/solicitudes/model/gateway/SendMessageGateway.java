package com.crediya.solicitudes.model.gateway;

import reactor.core.publisher.Mono;

public interface SendMessageGateway {
    Mono<String> send(String message);
}
