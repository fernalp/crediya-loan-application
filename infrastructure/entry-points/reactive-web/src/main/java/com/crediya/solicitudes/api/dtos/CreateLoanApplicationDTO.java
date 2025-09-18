package com.crediya.solicitudes.api.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateLoanApplicationDTO(
        @NotBlank(message = "El numero de identificacion es obligatorio")
        String numeroDeIdentificacion,
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal monto,
        @NotNull(message = "El plazo es obligatorio")
        @Min(value = 1, message = "El plazo debe ser mayor a cero")
        @Max(value = 360, message = "El plazo debe ser menor a 360")
        Integer plazoEnMeses,
        @NotBlank(message = "El tipo de credito es obligatorio")
        String tipoDeCredito
) {
}
