package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.constants.LoanConstants;
import com.crediya.solicitudes.model.customer.gateways.CustomerRepository;
import com.crediya.solicitudes.model.customer.gateways.JwtGateway;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanapplication.validations.LoanApplicationValidation;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.usecase.exceptions.CommunicationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final CustomerRepository customerRepository;
    private final JwtGateway jwtGateway;

    public Mono<LoanApplication> execute(LoanApplication loanApplication) {
        return this.validateLoanApplication(loanApplication)
                .flatMap(loanApplicationRepository::save);
    }

    private Mono<LoanApplication> validateLoanApplication(LoanApplication loanApplication) {
        return loanStatusRepository.findByName(LoanConstants.LOAN_STATUS_PENDING)
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
                }).flatMap(LoanApplicationValidation::validateBusinessRules)
                .switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND)));
    }

    private Mono<LoanApplication> validateCustomer(LoanApplication loanApplication){
        return customerRepository.findByIdNumber(loanApplication.getIdNumber(), loanApplication.getToken())
                .onErrorResume(error -> Mono.error(new CommunicationException(LoanConstants.ERROR_MESSAGE_CONNECTION_REFUSED)))
                .switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_CUSTOMER_NOT_FOUND)))
                .flatMap(customer -> {
                    if (customer.getEmail() == null) {
                        return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_CUSTOMER_NOT_FOUND));
                    }
                    return jwtGateway.extractUsername(loanApplication.getToken())
                                    .flatMap(username -> {
                                        if (!username.equals(customer.getEmail())) {
                                            return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_CONFLICT_USER));
                                        }
                                        loanApplication.setEmail(customer.getEmail());
                                        return Mono.just(loanApplication);
                                    });
                });
    }


}
