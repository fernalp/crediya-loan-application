package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.customer.gateways.CustomerRepository;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.usecase.exceptions.CommunicationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private static final String DEFAULT_LOAN_STATUS = "PENDIENTE";
    private static final String ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND = "El tipo de préstamo es inválido!";
    private static final String ERROR_MESSAGE_CUSTOMER_NOT_FOUND = "El cliente no se encuentra registrado!";
    private static final String ERROR_MESSAGE_CONNECTION_REFUSED = "Ah ocurrido un error, por favor contacte al administrador!";

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final CustomerRepository customerRepository;

    public Mono<LoanApplication> execute(LoanApplication loanApplication) {
        return this.validateLoanApplication(loanApplication)
                .flatMap(loanApplicationRepository::save);
    }

    private Mono<LoanApplication> validateLoanApplication(LoanApplication loanApplication) {
        return loanStatusRepository.findByName(DEFAULT_LOAN_STATUS)
                .flatMap(loanStatus -> {
                    loanApplication.setLoanStatus(loanStatus);
                    return Mono.just(loanApplication);
                })
                .flatMap(this::validateLoanType)
                .flatMap(this::validateCustomer)
                ;
    }
    private Mono<LoanApplication> validateLoanType(LoanApplication loanApplication){
        return loanTypeRepository.findByName(loanApplication.getLoanType().getName())
                .flatMap(loanType -> {
                    loanApplication.setLoanType(loanType);
                    return Mono.just(loanApplication);
                }).switchIfEmpty(Mono.error(new ValidationException(ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND)));
    }

    private Mono<LoanApplication> validateCustomer(LoanApplication loanApplication){
        return customerRepository.findByIdNumber(loanApplication.getIdNumber())
                .onErrorResume(error -> Mono.error(new CommunicationException(ERROR_MESSAGE_CONNECTION_REFUSED)))
                .switchIfEmpty(Mono.error(new ValidationException(ERROR_MESSAGE_CUSTOMER_NOT_FOUND)))
                .flatMap(customer -> {
                    if (customer.getEmail() == null) {
                        return Mono.error(new ValidationException(ERROR_MESSAGE_CUSTOMER_NOT_FOUND));
                    }
                    loanApplication.setEmail(customer.getEmail());
                    return Mono.just(loanApplication);
                });
    }
}
