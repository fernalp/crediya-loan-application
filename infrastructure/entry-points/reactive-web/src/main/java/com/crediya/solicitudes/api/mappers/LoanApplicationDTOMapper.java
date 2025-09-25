package com.crediya.solicitudes.api.mappers;

import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.LoanApplicationResponseAdvisorDTO;
import com.crediya.solicitudes.api.dtos.LoanApplicationResponseDTO;
import com.crediya.solicitudes.api.dtos.PageResponseDTO;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loantype.LoanType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static LoanApplicationResponseAdvisorDTO loanApplicationResponseAdvisorDTO(LoanApplication application) {
        return new LoanApplicationResponseAdvisorDTO(
                application.getId(),
                application.getAmount(),
                application.getTerm(),
                application.getEmail(),
                application.getCustomer().getFirstName() + " " + application.getCustomer().getLastName(),
                application.getLoanType().getName(),
                application.getLoanType().getInterestRate(),
                application.getLoanStatus().getName(),
                application.getCustomer().getSalary(),
                application.getMonthlyPayment()
        );
    }



    public static Mono<PageResponseDTO<LoanApplicationResponseAdvisorDTO>> toPageResponseDTO(PageResponse<LoanApplication> page) {
        return validateNull(page)
                .map(p -> {
                    List<LoanApplicationResponseAdvisorDTO> items = p.content().stream()
                            .map(LoanApplicationDTOMapper::loanApplicationResponseAdvisorDTO)
                            .collect(Collectors.toList());
                    return new PageResponseDTO<>(
                            p.page(),
                            p.size(),
                            p.totalElements(),
                            p.totalPages(),
                            items
                    );
                });
    }

    private static <T> Mono<T> validateNull(T target) {
        if (target == null) {
            Mono.error(new ValidationException("El valor no puede ser nulo"));
        }
        return Mono.just(Objects.requireNonNull(target));
    }

    public static Mono<LoanApplication> setToken(LoanApplication loanApplication, String jwt) {
        return validateNull(jwt)
                .map(token -> {
                    loanApplication.setToken(token);
                    return loanApplication;
                });
    }
}

