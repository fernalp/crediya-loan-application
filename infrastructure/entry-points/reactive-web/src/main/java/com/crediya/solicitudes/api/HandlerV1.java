package com.crediya.solicitudes.api;

import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.mappers.LoanApplicationDTOMapper;
import com.crediya.solicitudes.api.validator.ReactiveValidator;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandlerV1 {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;
    private final ReactiveValidator reactiveValidator;
    private final TransactionalOperator tx;

    public Mono<ServerResponse> createLoanApplication(ServerRequest serverRequest) {

        return serverRequest
                .bodyToMono(CreateLoanApplicationDTO.class)
                .switchIfEmpty(Mono.error(new ValidationException("El valor recibido no puede ser nulo")))
                .flatMap(reactiveValidator::validate)
                .flatMap(LoanApplicationDTOMapper::toLoanApplication)
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
}
