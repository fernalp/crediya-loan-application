package com.crediya.solicitudes.model.loanapplication.gateways;

import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface LoanApplicationRepository {

    Mono<LoanApplication> save(LoanApplication loanApplication);
    Flux<LoanApplication> findAllByFilter(PageFilter pageFilter);
    Mono<Long> countByLoanStatus(LoanStatus loanStatus);
    Mono<LoanApplication> findById(BigInteger id);
    Flux<LoanApplication> findByEmail(String email);
    Flux<LoanApplication> findByLoanType(LoanType loanType);
    Flux<LoanApplication> findByLoanStatus(LoanStatus loanStatus);

}
