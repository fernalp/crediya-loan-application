package com.crediya.solicitudes.api.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;

public record LoanApplicationResponseDTO(
        BigInteger id,
        BigDecimal monto,
        Integer plazoEnMeses,
        String correoElectronico,
        String tipoDeCredito,
        String estadoDeLaSolicitud
) {
}
