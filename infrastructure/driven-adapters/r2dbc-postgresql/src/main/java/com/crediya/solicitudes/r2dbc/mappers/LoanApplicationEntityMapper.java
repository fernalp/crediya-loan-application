package com.crediya.solicitudes.r2dbc.mappers;

import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.r2dbc.entities.LoanApplicationEntity;

public class LoanApplicationEntityMapper {

    private static final String ERROR_MESSAGE_LOAN_APPLICATION_NOT_FOUND = "Una solicitud de préstamo no puede ser nula";

    public static LoanApplicationEntity toEntity(LoanApplication loanApplication) {
        validateNull(loanApplication);
        return LoanApplicationEntity.builder()
                .id(loanApplication.getId())
                .amount(loanApplication.getAmount())
                .email(loanApplication.getEmail())
                .idLoanType(loanApplication.getLoanType().getId())
                .idLoanStatus(loanApplication.getLoanStatus().getId())
                .term(loanApplication.getTerm())
                .loanType(loanApplication.getLoanType())
                .loanStatus(loanApplication.getLoanStatus())
                .build();
    }

    public static LoanApplication toLoanApplication(LoanApplicationEntity loanApplicationEntity) {
        validateNull(loanApplicationEntity);
        return LoanApplication.builder()
                .id(loanApplicationEntity.getId())
                .amount(loanApplicationEntity.getAmount())
                .email(loanApplicationEntity.getEmail())
                .idLoanType(loanApplicationEntity.getIdLoanType())
                .idLoanStatus(loanApplicationEntity.getIdLoanStatus())
                .loanType(loanApplicationEntity.getLoanType())
                .loanStatus(loanApplicationEntity.getLoanStatus())
                .term(loanApplicationEntity.getTerm())
                .build();
    }

    private static void validateNull(Object object) {
        if (object == null) {
            throw new ValidationException(ERROR_MESSAGE_LOAN_APPLICATION_NOT_FOUND);
        }
    }
}
