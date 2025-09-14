package com.crediya.solicitudes.r2dbc.adapters;

import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.r2dbc.entities.LoanStatusEntity;
import com.crediya.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.solicitudes.r2dbc.repositories.LoanStatusReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanStatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanStatus,
        LoanStatusEntity,
        Integer,
        LoanStatusReactiveRepository
> implements LoanStatusRepository {
    public LoanStatusReactiveRepositoryAdapter(LoanStatusReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanStatus.class));
    }

    @Override
    public Mono<LoanStatus> findByName(String name) {
        return this.repository.findByName(name.trim().toUpperCase())
                .map(this::toEntity);
    }
}
