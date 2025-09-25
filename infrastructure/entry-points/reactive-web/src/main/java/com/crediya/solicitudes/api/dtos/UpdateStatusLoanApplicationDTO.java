package com.crediya.solicitudes.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigInteger;

public record UpdateStatusLoanApplicationDTO(
        @NotNull(message = "El id de la solicitud es obligatorio")
        @JsonProperty("loan_application_id")
        BigInteger loanApplicationId,
        @NotBlank(message = "El estado de la solicitud es obligatorio")
        @Pattern(regexp = "^(APROBADO|RECHAZADO)$", message = "El estado de la solicitud debe ser APROBADO o RECHAZADO")
        String status
) {
}
