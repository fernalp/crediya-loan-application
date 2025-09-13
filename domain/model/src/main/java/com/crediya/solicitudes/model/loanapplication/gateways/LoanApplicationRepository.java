package com.crediya.solicitudes.model.loanapplication.gateways;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface LoanApplicationRepository {

    Mono<LoanApplication> findById(BigInteger id);
    Flux<LoanApplication> findByEmail(String email);
    Flux<LoanApplication> findByLoanType(LoanType loanType);
    Flux<LoanApplication> findByLoanStatus(LoanStatus loanStatus);

}
