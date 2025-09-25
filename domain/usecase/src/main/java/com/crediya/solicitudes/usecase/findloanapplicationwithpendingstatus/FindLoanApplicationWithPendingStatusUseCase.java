package com.crediya.solicitudes.usecase.findloanapplicationwithpendingstatus;

import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.constants.LoanConstants;
import com.crediya.solicitudes.model.customer.Customer;
import com.crediya.solicitudes.model.customer.gateways.CustomerRepository;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class FindLoanApplicationWithPendingStatusUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final CustomerRepository customerRepository;

    public Mono<PageResponse<LoanApplication>> execute(PageFilter pageFilter, String token ) {
        if (pageFilter == null) {
            pageFilter = PageFilter.builder()
                    .page(LoanConstants.DEFAULT_PAGE_NUMBER)
                    .size(LoanConstants.DEFAULT_PAGE_SIZE)
                    .sort(LoanConstants.DEFAULT_SORT)
                    .direction(LoanConstants.DEFAULT_DIRECTION)
                    .build();
        }
        Mono<Long> total = getTotalLoanApplicationPending();
        Mono<Map<String,Customer>> customers = getCustomers(token);
        PageFilter finalPageFilter = pageFilter;
        Mono<List<LoanApplication>> loanApplications = customers
                .flatMapMany(customersMap ->
                loanApplicationRepository.findAllByFilter(finalPageFilter)
                        .flatMap(this::setLoanType)
                        .flatMap(this::setLoanStatus)
                        .map(loanApplication -> {
                            loanApplication.setCustomer(customersMap.get(loanApplication.getEmail()));
                            loanApplication.setMonthlyPayment(calculateMonthlyPayment(loanApplication.getAmount(), loanApplication.getLoanType().getInterestRate(), loanApplication.getTerm()));
                            return loanApplication;
                        }))
                .collectList();
        return Mono.zip(total, loanApplications)
                .map(tuple -> new PageResponse<>(
                        finalPageFilter.getPage(),
                        finalPageFilter.getSize(),
                        tuple.getT1(),
                        Math.ceilDiv(Math.toIntExact(tuple.getT1()), finalPageFilter.getSize()),
                        tuple.getT2()));
    }

    private Mono<Map<String, Customer>> getCustomers(String token) {
        return customerRepository.findAll(token)
                .doOnNext(customer -> System.out.println("Customer: " + customer.getEmail()))
                .collectMap(Customer::getEmail, customer -> customer);
    }

    private Mono<Long> getTotalLoanApplicationPending() {
        return loanStatusRepository.findByName(LoanConstants.LOAN_STATUS_PENDING)
                .flatMap(loanApplicationRepository::countByLoanStatus);
    }

    private Mono<LoanApplication> setLoanType(LoanApplication loanApplication) {
        return loanTypeRepository.findById(loanApplication.getIdLoanType())
                .flatMap(loanType -> {
                    loanApplication.setLoanType(loanType);
                    return Mono.just(loanApplication);
                }).switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND)));
    }

    private Mono<LoanApplication> setLoanStatus(LoanApplication loanApplication) {
        return loanStatusRepository.findByName(LoanConstants.LOAN_STATUS_PENDING)
                .flatMap(loanStatus -> {
                    loanApplication.setLoanStatus(loanStatus);
                    return Mono.just(loanApplication);
                }).switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_LOAN_STATUS_NOT_FOUND)));
    }

    /**
     * Se calcula el pago mensual usando la fórmula de amortización:
     * PMT = P * [r(1 + r)^n] / [(1 + r)^n - 1]
     * Donde: P = Principal (monto), r = tasa de interés mensual, n = número de pagos (meses)
     */
    private static BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, Integer termInMonths) {
        if (principal == null || annualRate == null || termInMonths == null ||
                principal.compareTo(BigDecimal.ZERO) <= 0 || termInMonths <= 0) {
            return BigDecimal.ZERO;
        }

        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(termInMonths), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRatePowerN = BigDecimal.ONE.add(monthlyRate)
                .pow(termInMonths, MathContext.DECIMAL128);

        BigDecimal numerator = monthlyRate.multiply(onePlusRatePowerN);

        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);

        BigDecimal monthlyPayment = principal.multiply(
                numerator.divide(denominator, 10, RoundingMode.HALF_UP)
        );

        return monthlyPayment.setScale(2, RoundingMode.HALF_UP);
    }

}
