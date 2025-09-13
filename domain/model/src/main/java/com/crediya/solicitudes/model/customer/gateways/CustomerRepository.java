package com.crediya.solicitudes.model.customer.gateways;

import com.crediya.solicitudes.model.customer.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> findByIdNumber(String idNumber);

}
