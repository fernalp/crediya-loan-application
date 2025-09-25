package com.crediya.solicitudes.api.openapi;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.api.dtos.CreateLoanApplicationDTO;
import com.crediya.solicitudes.api.dtos.LoanApplicationResponseDTO;
import com.crediya.solicitudes.api.dtos.PageResponseDTO;
import com.crediya.solicitudes.api.dtos.UpdateStatusLoanApplicationDTO;
import com.crediya.solicitudes.api.enums.SortField;
import com.crediya.solicitudes.api.enums.SortDirection;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.exampleobject.Builder.exampleOjectBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class LoanApplicationOpenApi {

    private static final String SUCCESS_CREATED = "Solicitud de préstamo creada exitosamente";
    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());

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
                                .responseCode(ApiConstants.HTTP_400)
                                .description(ApiConstants.DESCRIPTION_BAD_REQUEST)
                )
                ;
    }

    public Builder getAllLoanApplicationPending(Builder builder){
        return builder
                .operationId("getAllLoanApplicationPending")
                .description("Obtener lista paginada y filtrable de solicitudes de préstamos con estado pendiente")
                .tag("Solicitud de préstamo")
                .parameter(
                        parameterBuilder()
                                .in(ParameterIn.QUERY)
                                .name(ApiConstants.PARAMETER_PAGE)
                                .description(ApiConstants.DESCRIPTION_PAGE_PARAM)
                                .required(false)
                                .example(ApiConstants.DEFAULT_PAGE)
                )
                .parameter(
                        parameterBuilder()
                                .in(ParameterIn.QUERY)
                                .name(ApiConstants.PARAMETER_SIZE)
                                .description(ApiConstants.DESCRIPTION_SIZE_PARAM)
                                .required(false)
                                .example(ApiConstants.DEFAULT_SIZE)
                )
                .parameter(
                        parameterBuilder()
                                .in(ParameterIn.QUERY)
                                .name(ApiConstants.PARAMETER_SORT)
                                .description(ApiConstants.DESCRIPTION_SORT_PARAM + ". Campos permitidos: " + SortField.getValidFieldNamesAsString())
                                .required(false)
                                .example(ApiConstants.DEFAULT_SORT)
                )
                .parameter(
                        parameterBuilder()
                                .in(ParameterIn.QUERY)
                                .name(ApiConstants.PARAMETER_DIRECTION)
                                .description(ApiConstants.DESCRIPTION_DIRECTION_PARAM + ". Valores permitidos: " + SortDirection.getValidDirectionsAsString())
                                .required(false)
                                .example(ApiConstants.DEFAULT_DIRECTION)
                )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_200)
                                .description(ApiConstants.DESCRIPTION_SUCCESS_RESPONSE)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .schema(schemaBuilder().implementation(PageResponseDTO.class))
                                )
                )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_400)
                                .description(ApiConstants.DESCRIPTION_BAD_REQUEST)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .example(exampleOjectBuilder().value("{\"error\":\"Parámetros inválidos\",\"mensaje\":\"El parámetro 'pagina' debe ser un número entero mayor a 0\"}"))
                                )
                )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_500)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .example(exampleOjectBuilder().value("{\"error\":\"Error interno del servidor\",\"mensaje\":\"No fue posible obtener las solicitudes de préstamo en este momento. Intente nuevamente.\"}"))
                                )
                )
                ;
    }

    public Builder updateLoanApplicationStatus(Builder builder){
        return builder
                .operationId("updateLoanApplicationStatus")
                .description("Actualizar estado de una solicitud de préstamo")
                .tag("Solicitud de préstamo")
                .requestBody(
                        requestBodyBuilder()
                                .required(true)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .schema(schemaBuilder().implementation(UpdateStatusLoanApplicationDTO.class))
                                        )
                                )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_200)
                                .description(ApiConstants.DESCRIPTION_SUCCESS_RESPONSE)
                                .content(
                                        contentBuilder()
                                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                                .schema(schemaBuilder().implementation(LoanApplicationResponseDTO.class))
                                        )
                        )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_400)
                                .description(ApiConstants.DESCRIPTION_BAD_REQUEST)
                        )
                .response(
                        responseBuilder()
                                .responseCode(ApiConstants.HTTP_500)
                                .description(ApiConstants.DESCRIPTION_INTERNAL_ERROR)
                        )
                ;
    }

}
