package com.crediya.solicitudes.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;

public record LoanApplicationResponseAdvisorDTO(
        BigInteger id,
        BigDecimal monto,
        @JsonProperty("plazo_en_meses")
        Integer plazoEnMeses,
        @JsonProperty("correo_electronico")
        String correoElectronico,
        @JsonProperty("nombre")
        String nombre,
        @JsonProperty("tipo_prestamo")
        String tipoPrestamo,
        @JsonProperty("tasa_interes")
        BigDecimal tasaInteres,
        @JsonProperty("estado_solicitud")
        String estadoSolicitud,
        @JsonProperty("salario_base")
        BigDecimal salarioBase,
        @JsonProperty("monto_mensual_solicitud")
        BigDecimal montoMensualSolicitud
) {

}
