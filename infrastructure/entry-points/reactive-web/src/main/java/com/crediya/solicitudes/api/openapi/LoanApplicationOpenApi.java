package com.crediya.solicitudes.api.openapi;

import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.LoanApplicationResponseDTO;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class LoanApplicationOpenApi {

    private static final String SUCCESS_CREATED = "Solicitud de préstamo creada exitosamente";
    private static final String BAD_REQUEST = HttpStatus.BAD_REQUEST.getReasonPhrase();
    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());

    public Builder createLoanApplication(Builder builder) {
        return builder
                .operationId("savedLoanApplication")
                .description("Crear una solicitud de préstamo")
                .tag("Solicitud de préstamo")
                .requestBody(
                        requestBodyBuilder()
                                .required(true)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .schema(schemaBuilder().implementation(CreateLoanApplicationDTO.class))
                                )
                ).response(
                        responseBuilder()
                                .responseCode(CREATED_CODE)
                                .description(SUCCESS_CREATED)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .schema(schemaBuilder().implementation(LoanApplicationResponseDTO.class))
                                )
                ).response(
                        responseBuilder()
                                .responseCode(BAD_REQUEST_CODE)
                                .description(BAD_REQUEST)
                )
                ;
    }


}
