package com.crediya.solicitudes.api;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.PageResponseDTO;
import com.crediya.solicitudes.api.mappers.LoanApplicationDTOMapper;
import com.crediya.solicitudes.api.validator.PaginationValidator;
import com.crediya.solicitudes.api.validator.ReactiveValidator;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import com.crediya.solicitudes.usecase.findloanapplicationwithpendingstatus.FindLoanApplicationWithPendingStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandlerV1 {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;
    private final FindLoanApplicationWithPendingStatusUseCase findLoanApplicationWithPendingStatusUseCase;
    private final ReactiveValidator reactiveValidator;
    private final TransactionalOperator tx;

    @PreAuthorize("hasRole('CUSTOMER')")
    public Mono<ServerResponse> createLoanApplication(ServerRequest serverRequest) {

        String token = extractToken(serverRequest);

        return serverRequest
                .bodyToMono(CreateLoanApplicationDTO.class)
                .switchIfEmpty(Mono.error(new ValidationException("El valor recibido no puede ser nulo")))
                .flatMap(reactiveValidator::validate)
                .flatMap(LoanApplicationDTOMapper::toLoanApplication)
                .flatMap(loanApplication -> LoanApplicationDTOMapper.setToken(loanApplication, token))
                .flatMap(createLoanApplicationUseCase::execute)
                .as(tx::transactional)
                .flatMap(LoanApplicationDTOMapper::toLoanApplicationResponseDTO)
                .flatMap(responseDTO ->
                {
                    log.info("Solicitud de préstamo creada exitosamente " + responseDTO);
                    return ServerResponse
                            .status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDTO);
                })
                .doOnError(error -> log.error(error.toString()));
    }

    @PreAuthorize("hasRole('ADVISOR')")
    public Mono<ServerResponse> getAllLoanApplicationPending(ServerRequest serverRequest){

        String token = extractToken(serverRequest);

        log.info("Iniciando consulta de solicitudes pendientes");
        
        return PaginationValidator.validateAndExtractPageFilter(serverRequest)
                .doOnNext(filter -> 
                    log.info("Parámetros validados: página={}, cantidad={}, ordenar={}, dirección={}", 
                            filter.getPage(), filter.getSize(), filter.getSort(), filter.getDirection()))
                .flatMap(filter -> findLoanApplicationWithPendingStatusUseCase.execute(filter, token))
                .doOnNext(pageResponse -> 
                    log.info("Consulta exitosa: {} solicitudes encontradas en página {} de {}",
                            pageResponse.totalElements(), pageResponse.page(), pageResponse.totalPages()))
                .flatMap(LoanApplicationDTOMapper::toPageResponseDTO)
                .flatMap((PageResponseDTO<?> data) -> {
                    log.info("Respuesta enviada exitosamente con {} elementos", data.items().size());
                    return ServerResponse
                            .status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(data);
                })
                .doOnError(error -> log.error("Error al consultar solicitudes pendientes: {}", error.getMessage(), error))
                .onErrorResume(error -> {
                    if (error instanceof ValidationException) {
                        log.warn("Error de validación en parámetros: {}", error.getMessage());
                        return ServerResponse
                                .status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(java.util.Map.of(
                                        "error", "Parámetros inválidos",
                                        "mensaje", error.getMessage()
                                ));
                    } else {
                        log.error("Error procesando solicitud de listado de préstamos pendientes", error);
                        return ServerResponse
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(java.util.Map.of(
                                        "error", ApiConstants.ERROR_INTERNAL_SERVER,
                                        "mensaje", ApiConstants.ERROR_LOAN_APPLICATIONS_UNAVAILABLE
                                ));
                    }
                });

    }

    private static String extractToken(ServerRequest serverRequest) {
        return Objects.requireNonNull(serverRequest.headers().asHttpHeaders().getFirst(ApiConstants.HEADER_AUTHORIZATION)).substring(ApiConstants.TOKEN_PREFIX_LENGTH);
    }
}
