package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.customer.Customer;
import com.crediya.solicitudes.model.customer.gateways.CustomerRepository;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.model.loantype.LoanType;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.usecase.exceptions.CommunicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLoanApplicationUseCaseTest {

    @InjectMocks
    private CreateLoanApplicationUseCase createLoanApplicationUseCase;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private LoanStatusRepository loanStatusRepository;

    @Mock
    private CustomerRepository customerRepository;

    private final String DEFAULT_LOAN_STATUS = "PENDIENTE";
    private final String DEFAULT_LOAN_TYPE = "PERSONAL";
    private final String DEFAULT_ID_NUMBER = "123456789";
    private final String DEFAULT_EMAIL = "email@test.com";

    private final LoanType fakeLoanType = LoanType
            .builder()
            .id(1)
            .name(DEFAULT_LOAN_TYPE)
            .build();

    private final LoanStatus fakeLoanStatus = LoanStatus
            .builder()
            .id(1)
            .name(DEFAULT_LOAN_STATUS)
            .build();

    private final LoanApplication fakeLoanApplication = LoanApplication
            .builder()
            .id(BigInteger.ONE)
            .idNumber(DEFAULT_ID_NUMBER)
            .loanType(fakeLoanType)
            .loanStatus(fakeLoanStatus)
            .build();



    private final Customer fakeCustomer = Customer
            .builder()
            .idNumber(DEFAULT_ID_NUMBER)
            .email(DEFAULT_EMAIL)
            .build();

    @Test
    void shouldCreateLoanApplicationWhenAllDataIsCorrect() {
        when(loanTypeRepository.findByName(fakeLoanApplication.getLoanType().getName())).thenReturn(Mono.just(fakeLoanType));
        when(loanStatusRepository.findByName(fakeLoanApplication.getLoanStatus().getName())).thenReturn(Mono.just(fakeLoanStatus));
        when(customerRepository.findByIdNumber(fakeLoanApplication.getIdNumber(), anyString())).thenReturn(Mono.just(fakeCustomer));
        when(loanApplicationRepository.save(fakeLoanApplication)).thenReturn(Mono.just(fakeLoanApplication));

        Mono<LoanApplication> result = createLoanApplicationUseCase.execute(fakeLoanApplication);

        StepVerifier.create(result)
                .expectNext(fakeLoanApplication)
                .expectComplete()
                .verify();
    }

    @Test
    void shouldThrowValidationExceptionWhenLoanTypeIsNotFound() {
        when(loanStatusRepository.findByName(anyString())).thenReturn(Mono.just(fakeLoanStatus));
        when(loanTypeRepository.findByName(fakeLoanApplication.getLoanType().getName())).thenReturn(Mono.empty());

        Mono<LoanApplication> result = createLoanApplicationUseCase.execute(fakeLoanApplication);

        StepVerifier.create(result)
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    void shouldThrowValidationExceptionWhenCustomerIsNotFound() {
        String message = "message";
        when(loanStatusRepository.findByName(anyString())).thenReturn(Mono.just(fakeLoanStatus));
        when(loanTypeRepository.findByName(anyString())).thenReturn(Mono.just(fakeLoanType));
        when(customerRepository.findByIdNumber(fakeLoanApplication.getIdNumber(), anyString())).thenReturn(Mono.empty());

        Mono<LoanApplication> result = createLoanApplicationUseCase.execute(fakeLoanApplication);

        StepVerifier.create(result)
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    void shouldThrowCommunicationExceptionWhenCustomerRepositoryFails() {
        String message = "message";
        when(loanStatusRepository.findByName(anyString())).thenReturn(Mono.just(fakeLoanStatus));
        when(loanTypeRepository.findByName(anyString())).thenReturn(Mono.just(fakeLoanType));
        when(customerRepository.findByIdNumber(anyString(), anyString())).thenReturn(Mono.error(new CommunicationException(message)));

        Mono<LoanApplication> result = createLoanApplicationUseCase.execute(fakeLoanApplication);

        StepVerifier.create(result)
                .expectError(CommunicationException.class)
                .verify();
    }



}