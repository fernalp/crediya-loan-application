package com.crediya.solicitudes.api.mappers;

import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.LoanApplicationResponseDTO;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class LoanApplicationDTOMapper {

    public static Mono<LoanApplication> toLoanApplication(CreateLoanApplicationDTO createLoanApplicationDTO) {
        return validateNull(createLoanApplicationDTO)
                .map(dto -> LoanApplication.builder()
                        .amount(dto.monto())
                        .idNumber(dto.numeroDeIdentificacion())
                        .term(dto.plazoEnMeses())
                        .loanType(LoanType.builder().name(dto.tipoDeCredito()).build())
                        .build());
    }

    public static Mono<LoanApplicationResponseDTO> toLoanApplicationResponseDTO(LoanApplication loanApplication) {
        return validateNull(loanApplication)
                .map(application -> new LoanApplicationResponseDTO(
                        application.getId(),
                        application.getAmount(),
                        application.getTerm(),
                        application.getEmail(),
                        application.getLoanType().getName(),
                        application.getLoanStatus().getName()
                ));
    }

    private static <T> Mono<T> validateNull(T target) {
        if (target == null) {
            Mono.error(new ValidationException("El valor no puede ser nulo"));
        }
        return Mono.just(Objects.requireNonNull(target));
    }
}

