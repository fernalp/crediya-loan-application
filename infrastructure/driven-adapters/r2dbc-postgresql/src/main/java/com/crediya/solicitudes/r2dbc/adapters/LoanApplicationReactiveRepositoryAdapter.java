package com.crediya.solicitudes.r2dbc.adapters;

import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import com.crediya.solicitudes.r2dbc.entities.LoanApplicationEntity;
import com.crediya.solicitudes.r2dbc.mappers.LoanApplicationEntityMapper;
import com.crediya.solicitudes.r2dbc.repositories.LoanApplicationReactiveRepository;
import com.crediya.solicitudes.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class LoanApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication,
        LoanApplicationEntity,
        BigInteger,
        LoanApplicationReactiveRepository
> implements LoanApplicationRepository {
    public LoanApplicationReactiveRepositoryAdapter(LoanApplicationReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanApplication.class));
    }

    @Override
    public Mono<LoanApplication> save(LoanApplication loanApplication) {
        return this.repository
                .save(LoanApplicationEntityMapper.toEntity(loanApplication))
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Flux<LoanApplication> findAllByFilter(PageFilter pageFilter) {
        int limit = pageFilter.getSize();
        int offset = pageFilter.getPage() * (limit - 1);
        return this.repository.findAllByPendingPage(limit, offset)
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Mono<Long> countByLoanStatus(LoanStatus loanStatus) {
        return this.repository.countByLoanStatus(loanStatus.getId());
    }

    @Override
    public Flux<LoanApplication> findByEmail(String email) {
        return this.repository
                .findByEmail(email)
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Flux<LoanApplication> findByLoanType(LoanType loanType) {
        return this.repository
                .findByLoanType(loanType.getId())
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Flux<LoanApplication> findByLoanStatus(LoanStatus loanStatus) {
        return this.repository
                .findByLoanStatus(loanStatus.getId())
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Flux<LoanApplication> findAll() {
        return this.repository.findAll()
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }

    @Override
    public Mono<LoanApplication> findById(BigInteger id) {
        return this.repository.findById(id)
                .map(LoanApplicationEntityMapper::toLoanApplication);
    }
}
