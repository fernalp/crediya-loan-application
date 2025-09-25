package com.crediya.solicitudes.model.customer.gateways;

import com.crediya.solicitudes.model.customer.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> findByIdNumber(String idNumber, String token);
    Flux<Customer> findAll(String token);

}
