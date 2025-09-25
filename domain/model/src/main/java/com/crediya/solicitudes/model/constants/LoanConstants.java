package com.crediya.solicitudes.model.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoanConstants {

    public static final String LOAN_STATUS_PENDING = "PENDIENTE";
    public static final String LOAN_STATUS_APPROVED = "APROBADO";
    public static final String LOAN_STATUS_REJECTED = "RECHAZADO";
    public static final String LOAN_STATUS_IN_REVIEW = "EN_REVISION";

    public static final String LOAN_TYPE_PERSONAL = "PERSONAL";
    public static final String LOAN_TYPE_VEHICLE = "VEHICULAR";
    public static final String LOAN_TYPE_MORTGAGE = "HIPOTECARIO";
    public static final String LOAN_TYPE_BUSINESS = "EMPRESARIAL";


    public static final BigDecimal MIN_LOAN_AMOUNT = BigDecimal.valueOf(100000); // 100k
    public static final BigDecimal MAX_LOAN_AMOUNT = BigDecimal.valueOf(500000000); // 500M
    public static final int MIN_LOAN_TERM_MONTHS = 1;
    public static final int MAX_LOAN_TERM_MONTHS = 360; // 30 años


    public static final String ERROR_CODE_VALIDATION = "VALIDATION_ERROR";
    public static final String ERROR_CODE_BUSINESS_RULE = "BUSINESS_RULE_ERROR";
    public static final String ERROR_CODE_COMMUNICATION = "COMMUNICATION_ERROR";
    public static final String ERROR_CODE_NOT_FOUND = "NOT_FOUND_ERROR";
    public static final String ERROR_CODE_CONFLICT = "CONFLICT_ERROR";


    public static final String ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND = "El tipo de préstamo es inválido";
    public static final String ERROR_MESSAGE_CUSTOMER_NOT_FOUND = "El cliente no se encuentra registrado";
    public static final String ERROR_MESSAGE_CONNECTION_REFUSED = "Ha ocurrido un error, por favor contacte al administrador";
    public static final String ERROR_MESSAGE_CONFLICT_USER = "No puede solicitar un préstamo para un usuario diferente al que se encuentra autenticado";
    public static final String ERROR_MESSAGE_LOAN_STATUS_NOT_FOUND = "El estado de la solicitud no es válido";

    public static final String ERROR_MESSAGE_AMOUNT_TOO_LOW = "El monto mínimo de préstamo es ";
    public static final String ERROR_MESSAGE_AMOUNT_TOO_HIGH = "El monto máximo de préstamo es ";
    public static final String ERROR_MESSAGE_TERM_TOO_SHORT = "El plazo mínimo es de 1 mes";
    public static final String ERROR_MESSAGE_TERM_TOO_LONG = "El plazo máximo es de 360 meses";
    public static final String ERROR_MESSAGE_ACTIVE_LOAN_EXISTS = "Ya tiene una solicitud de préstamo activa";
    public static final String ERROR_MESSAGE_INSUFFICIENT_INCOME = "Su salario no es suficiente para el monto solicitado";
    public static final String ERROR_MESSAGE_INVALID_TOKEN = "El token es inválido, por favor intente iniciar sesión de nuevo!";

    public static final String VALIDATION_ID_NUMBER_REQUIRED = "El número de identificación es obligatorio";
    public static final String VALIDATION_AMOUNT_REQUIRED = "El monto es obligatorio";
    public static final String VALIDATION_AMOUNT_POSITIVE = "El monto debe ser mayor a cero";
    public static final String VALIDATION_TERM_REQUIRED = "El plazo es obligatorio";
    public static final String VALIDATION_TERM_MIN = "El plazo debe ser mayor a cero";
    public static final String VALIDATION_TERM_MAX = "El plazo debe ser menor a 360";
    public static final String VALIDATION_LOAN_TYPE_REQUIRED = "El tipo de crédito es obligatorio";

    public static final String LOG_LOAN_APPLICATION_CREATED = "Solicitud de préstamo creada exitosamente: {}";
    public static final String LOG_LOAN_APPLICATION_FOUND = "Solicitudes encontradas: {} elementos";
    public static final String LOG_VALIDATING_CUSTOMER = "Validando información del cliente con ID: {}";
    public static final String LOG_VALIDATING_LOAN_TYPE = "Validando tipo de préstamo: {}";
    public static final String LOG_ERROR_OCCURRED = "Error occurred: {}";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String ROLE_CLAIM = "role";
    public static final String AUTHORITY_PREFIX = "ROLE_";

    public static final String[] AUTH_WHITELIST = {
            "/api/doc/swagger-ui.html",
            "/api/doc/api-docs/**",
            "/api/doc/swagger-ui/**",
            "/docs",
            "/scalar/**"
    };

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_DIRECTION = "ASC";

    public static final String NO_NULL = "El valor ingresado no puede ser nulo";
}