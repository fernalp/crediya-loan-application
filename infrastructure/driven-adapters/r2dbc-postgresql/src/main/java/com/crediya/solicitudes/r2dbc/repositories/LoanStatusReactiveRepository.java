package com.crediya.solicitudes.r2dbc.repositories;

import com.crediya.solicitudes.r2dbc.entities.LoanStatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface LoanStatusReactiveRepository extends ReactiveCrudRepository<LoanStatusEntity, Integer>, ReactiveQueryByExampleExecutor<LoanStatusEntity> {

    Mono<LoanStatusEntity> findByName(String name);

}
