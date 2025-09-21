package com.crediya.solicitudes.r2dbc.repositories;

import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.r2dbc.entities.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

    Flux<LoanApplicationEntity> findByEmail(String email);
    Flux<LoanApplicationEntity> findByLoanType(Integer idLoanType);
    Flux<LoanApplicationEntity> findByLoanStatus(Integer idLoanStatus);
    @Query("SELECT COUNT(*) FROM loan_application WHERE id_loan_status = :idLoanStatus")
    Mono<Long> countByLoanStatus(Integer idLoanStatus);
    @Query("SELECT * FROM loan_application WHERE id_loan_status = 'PENDIENTE' LIMIT :limit OFFSET :offset")
    Flux<LoanApplicationEntity> findAllByPendingPage(Integer limit, Integer offset);

}
