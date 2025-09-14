package com.crediya.solicitudes.r2dbc.repositories;

import com.crediya.solicitudes.r2dbc.entities.LoanApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.math.BigInteger;


public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    Flux<LoanApplicationEntity> findByEmail(String email);
    Flux<LoanApplicationEntity> findByLoanType(Integer idLoanType);
    Flux<LoanApplicationEntity> findByLoanStatus(Integer idLoanStatus);

}
