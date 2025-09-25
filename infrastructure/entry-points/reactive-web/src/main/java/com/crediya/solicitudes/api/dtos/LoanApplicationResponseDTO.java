package com.crediya.solicitudes.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;

public record LoanApplicationResponseDTO(
        BigInteger id,
        BigDecimal monto,
        @JsonProperty("plazo_en_meses")
        Integer plazoEnMeses,
        @JsonProperty("correo_electronico")
        String correoElectronico,
        @JsonProperty("tipo_de_credito")
        String tipoDeCredito,
        @JsonProperty("estado_de_la_solicitud")
        String estadoDeLaSolicitud
) {
}
