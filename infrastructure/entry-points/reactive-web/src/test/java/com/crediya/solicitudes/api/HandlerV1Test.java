package com.crediya.solicitudes.api;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.customer.Customer;
import com.crediya.solicitudes.model.loantype.LoanType;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.usecase.findloanapplicationwithpendingstatus.FindLoanApplicationWithPendingStatusUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerV1Test {

    @Mock
    private FindLoanApplicationWithPendingStatusUseCase findLoanApplicationWithPendingStatusUseCase;

    private HandlerV1 handlerV1;

    @BeforeEach
    void setUp() {
        handlerV1 = new HandlerV1(null, findLoanApplicationWithPendingStatusUseCase, null, null);
    }

    @Test
    void getAllLoanApplicationPending_withValidParameters_shouldReturnSuccessResponse() {
        // Given
        LoanApplication loanApplication = createSampleLoanApplication();
        List<LoanApplication> applications = Arrays.asList(loanApplication);
        PageResponse<LoanApplication> pageResponse = new PageResponse<>(1, 10, 1L, 1, applications);
        
        when(findLoanApplicationWithPendingStatusUseCase.execute(any(PageFilter.class), anyString()))
                .thenReturn(Mono.just(pageResponse));

        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_PAGE, "1")
                .queryParam(ApiConstants.PARAMETER_SIZE, "10")
                .queryParam(ApiConstants.PARAMETER_SORT, "id")
                .queryParam(ApiConstants.PARAMETER_DIRECTION, "ASC")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withDefaultParameters_shouldReturnSuccessResponse() {
        // Given
        LoanApplication loanApplication = createSampleLoanApplication();
        List<LoanApplication> applications = Arrays.asList(loanApplication);
        PageResponse<LoanApplication> pageResponse = new PageResponse<>(1, 10, 1L, 1, applications);
        
        when(findLoanApplicationWithPendingStatusUseCase.execute(any(PageFilter.class), anyString()))
                .thenReturn(Mono.just(pageResponse));

        MockServerRequest request = MockServerRequest.builder().build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withInvalidPage_shouldReturnBadRequest() {
        // Given
        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_PAGE, "0")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withInvalidSize_shouldReturnBadRequest() {
        // Given
        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_SIZE, "101")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withInvalidDirection_shouldReturnBadRequest() {
        // Given
        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_DIRECTION, "INVALID")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withInvalidSortField_shouldReturnBadRequest() {
        // Given
        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_SORT, "invalidField")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withUseCaseError_shouldReturnInternalServerError() {
        // Given
        when(findLoanApplicationWithPendingStatusUseCase.execute(any(PageFilter.class), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_PAGE, "1")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .verifyComplete();
    }

    @Test
    void getAllLoanApplicationPending_withNonNumericPage_shouldReturnBadRequest() {
        // Given
        MockServerRequest request = MockServerRequest.builder()
                .queryParam(ApiConstants.PARAMETER_PAGE, "abc")
                .build();

        // When
        Mono<ServerResponse> result = handlerV1.getAllLoanApplicationPending(request);

        // Then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();
    }

    private LoanApplication createSampleLoanApplication() {
        Customer customer = Customer.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .salary(new BigDecimal("3000000"))
                .build();

        LoanType loanType = LoanType.builder()
                .name("Personal")
                .interestRate(new BigDecimal("15.5"))
                .build();

        LoanStatus loanStatus = LoanStatus.builder()
                .name("PENDIENTE")
                .build();

        return LoanApplication.builder()
                .id(BigInteger.ONE)
                .amount(new BigDecimal("1000000"))
                .term(12)
                .email("juan.perez@email.com")
                .customer(customer)
                .loanType(loanType)
                .loanStatus(loanStatus)
                .build();
    }
}