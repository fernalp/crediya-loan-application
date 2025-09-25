package com.crediya.solicitudes.model.loanapplication.validations;

import com.crediya.solicitudes.model.constants.LoanConstants;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class LoanApplicationValidation {

    public static Mono<LoanApplication> validateBusinessRules(LoanApplication loanApplication) {
        return Mono.when(
                validateAmountLimits(loanApplication.getAmount(), loanApplication.getLoanType()),
                validateTermLimits(loanApplication.getTerm())
        ).thenReturn(loanApplication);
    }

    private static Mono<Void> validateAmountLimits(BigDecimal amount, LoanType loanType) {
        if (amount.compareTo(loanType.getMinimumAmount()) < 0) {
            return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_AMOUNT_TOO_LOW + LoanConstants.MIN_LOAN_AMOUNT));
        }
        if (amount.compareTo(loanType.getMaximumAmount()) > 0) {
            return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_AMOUNT_TOO_HIGH + loanType.getMaximumAmount()));
        }
        return Mono.empty();
    }

    private static Mono<Void> validateTermLimits(Integer termInMonths) {
        if (termInMonths < LoanConstants.MIN_LOAN_TERM_MONTHS) {
            return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_TERM_TOO_SHORT));
        }

        if (termInMonths > LoanConstants.MAX_LOAN_TERM_MONTHS) {
            return Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_TERM_TOO_LONG));
        }

        return Mono.empty();
    }
}