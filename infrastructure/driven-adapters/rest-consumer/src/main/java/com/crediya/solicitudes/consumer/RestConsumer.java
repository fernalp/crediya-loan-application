package com.crediya.solicitudes.consumer;

import com.crediya.solicitudes.consumer.mappers.CustomerRestMapper;
import com.crediya.solicitudes.model.constants.LoanConstants;
import com.crediya.solicitudes.model.customer.Customer;
import com.crediya.solicitudes.model.customer.gateways.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestConsumer implements CustomerRepository {

    private final WebClient client;

    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
//    @CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/)
//    public Mono<CustomerResponse> testGet() {
//        return client
//                .get()
//                .retrieve()
//                .bodyToMono(CustomerResponse.class);
//    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    @CircuitBreaker(name = "getCustomerByIdNumber")
    @Override
    public Mono<Customer> findByIdNumber(String idNumber, String token) {
        return client
                .get()
                .uri("/" + idNumber)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                .onStatus(Objects::isNull, response -> Mono.empty())
                .bodyToMono(CustomerResponse.class)
                .map(CustomerRestMapper::toCustomer);
    }

    @CircuitBreaker(name = "getAllCustomer")
    @Override
    public Flux<Customer> findAll(String token) {
        return client
                .get()
                .header(LoanConstants.AUTHORIZATION_HEADER, LoanConstants.BEARER_PREFIX + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                .onStatus(Objects::isNull, response -> Mono.empty())
                .bodyToFlux(CustomerResponse.class)
                .map(customerResponse -> {
                    log.info("CustomerResponse: {}", customerResponse);
                    return CustomerRestMapper.toCustomer(customerResponse);
                });
    }
}
