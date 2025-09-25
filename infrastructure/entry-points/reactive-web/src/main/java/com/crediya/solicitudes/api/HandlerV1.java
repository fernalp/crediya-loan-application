package com.crediya.solicitudes.api;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.PageResponseDTO;
import com.crediya.solicitudes.api.dtos.UpdateStatusLoanApplicationDTO;
import com.crediya.solicitudes.api.mappers.LoanApplicationDTOMapper;
import com.crediya.solicitudes.api.validator.PaginationValidator;
import com.crediya.solicitudes.api.validator.ReactiveValidator;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import com.crediya.solicitudes.usecase.findloanapplicationwithpendingstatus.FindLoanApplicationWithPendingStatusUseCase;
import com.crediya.solicitudes.usecase.updatestatusloanapplication.UpdateStatusLoanApplicationUseCase;
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
    private final UpdateStatusLoanApplicationUseCase updateStatusLoanApplicationUseCase;
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
                .doOnError(error -> log.error("Error al consultar solicitudes pendientes: {}", error.getMessage()));
    }

    @PreAuthorize("hasRole('ADVISOR')")
    public Mono<ServerResponse> updateLoanApplicationStatus(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("loanApplicationId");

        return serverRequest.bodyToMono(UpdateStatusLoanApplicationDTO.class)
                .flatMap(reactiveValidator::validate)
                .flatMap(dto -> {
                    if (!dto.loanApplicationId().toString().equals(id)) {
                        return Mono.error(new ValidationException("El id de la solicitud no coincide con el id proporcionado"));
                    }
                    return updateStatusLoanApplicationUseCase.execute(dto.loanApplicationId(), dto.status());
                })
                .as(tx::transactional)
                .flatMap(LoanApplicationDTOMapper::toLoanApplicationResponseDTO)
                .flatMap(responseDTO -> {
                    log.info("Solicitud de préstamo actualizada exitosamente " + responseDTO);
                    return ServerResponse
                            .status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDTO);
                })
                .doOnError(error -> log.error("Error al actualizar estado de solicitud de préstamo: {}", error.getMessage()));
    }


    private static String extractToken(ServerRequest serverRequest) {
        return Objects.requireNonNull(serverRequest.headers().asHttpHeaders().getFirst(ApiConstants.HEADER_AUTHORIZATION)).substring(ApiConstants.TOKEN_PREFIX_LENGTH);
    }
}
