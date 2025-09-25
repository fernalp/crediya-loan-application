package com.crediya.solicitudes.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Builder(toBuilder = true)
public record CustomerResponse(
        BigInteger id,
        @JsonProperty("numero_identificacion")
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        @JsonProperty("fecha_nacimiento")
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        @JsonProperty("correo_electronico")
        String correoElectronico,
        BigDecimal salario,
        @JsonProperty("id_rol")
        Integer idRol
) {

}