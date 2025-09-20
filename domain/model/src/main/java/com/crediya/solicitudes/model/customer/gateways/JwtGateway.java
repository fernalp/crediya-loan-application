package com.crediya.solicitudes.model.customer.gateways;

import reactor.core.publisher.Mono;

public interface JwtGateway {

    Mono<String> extractUsername(String token);
}
