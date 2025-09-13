package com.crediya.solicitudes.model.loantype.gateways;

import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {

    Mono<LoanType> findById(Integer id);
    Mono<LoanType> findByName(String name);
    Flux<LoanType> findAll();
}
