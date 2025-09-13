package com.crediya.solicitudes.model.loanstatus.gateways;

import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanStatusRepository {

    Mono<LoanStatus> findById(Integer id);
    Mono<LoanStatus> findByName(String name);
    Flux<LoanStatus> findAll();
}
