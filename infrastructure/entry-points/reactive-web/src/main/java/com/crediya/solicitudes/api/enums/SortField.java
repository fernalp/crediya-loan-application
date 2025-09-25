package com.crediya.solicitudes.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum SortField {
    ID("id", "Identificador único de la solicitud"),
    MONTO("monto", "Monto solicitado del préstamo"),
    PLAZO("plazoEnMeses", "Plazo en meses del préstamo"),
    NOMBRE("nombre", "Nombre completo del cliente"),
    TIPO_PRESTAMO("tipoPrestamo", "Tipo de préstamo"),
    TASA_INTERES("tasaInteres", "Tasa de interés del préstamo"),
    ESTADO_SOLICITUD("estadoSolicitud", "Estado actual de la solicitud"),
    SALARIO_BASE("salarioBase", "Salario base del cliente"),
    MONTO_MENSUAL("montoMensualSolicitud", "Monto mensual calculado");

    private final String fieldName;
    private final String description;

    /**
     * Valida si el campo de ordenamiento es válido
     */
    public static boolean isValidField(String fieldName) {
        return Arrays.stream(values())
                .anyMatch(field -> field.getFieldName().equals(fieldName));
    }

    /**
     * Obtiene todos los nombres de campos válidos como Set
     */
    public static Set<String> getValidFieldNames() {
        return Arrays.stream(values())
                .map(SortField::getFieldName)
                .collect(Collectors.toSet());
    }

    /**
     * Obtiene todos los nombres de campos válidos como String separado por comas
     */
    public static String getValidFieldNamesAsString() {
        return Arrays.stream(values())
                .map(SortField::getFieldName)
                .collect(Collectors.joining(", "));
    }

    /**
     * Obtiene el SortField por nombre de campo
     */
    public static SortField fromFieldName(String fieldName) {
        return Arrays.stream(values())
                .filter(field -> field.getFieldName().equals(fieldName))
                .findFirst()
                .orElse(ID); // valor por defecto
    }
}