package com.crediya.solicitudes.api.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {

    public static final String API_V1 = "/api/v1";
    public static final String PATH_API_LOAN_APPLICATIONS = API_V1 + "/solicitudes";

    public static final String PARAMETER_PAGE = "pagina";
    public static final String PARAMETER_SIZE = "cantidad";
    public static final String PARAMETER_SORT = "ordenar";
    public static final String PARAMETER_DIRECTION = "direccion";

    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";
    public static final String DEFAULT_SORT = "id";
    public static final String DEFAULT_DIRECTION = "ASC";

    public static final int MIN_PAGE = 1;
    public static final int MAX_PAGE = 10000;
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 100;

    public static final String ERROR_INVALID_PAGE = "El parámetro 'pagina' debe ser un número entero mayor a 0";
    public static final String ERROR_INVALID_SIZE = "El parámetro 'cantidad' debe ser un número entero entre 1 y 100";
    public static final String ERROR_INVALID_DIRECTION = "El parámetro 'direccion' debe ser 'ASC' o 'DESC'";
    public static final String ERROR_INVALID_SORT_FIELD = "El parámetro 'ordenar' debe ser uno de los campos permitidos";
    public static final String ERROR_PAGE_OUT_OF_BOUNDS = "El número de página solicitado excede el límite máximo";
    
    public static final String ERROR_INTERNAL_SERVER = "Error interno del servidor";
    public static final String ERROR_LOAN_APPLICATIONS_UNAVAILABLE = "No fue posible obtener las solicitudes de préstamo en este momento. Intente nuevamente.";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final int TOKEN_PREFIX_LENGTH = 7; // "Bearer "

    public static final String HTTP_200 = "200";
    public static final String HTTP_400 = "400";
    public static final String HTTP_500 = "500";

    public static final String DESCRIPTION_PAGE_PARAM = "Número de página (mínimo: 1, máximo: 10000)";
    public static final String DESCRIPTION_SIZE_PARAM = "Cantidad de elementos por página (mínimo: 1, máximo: 100)";
    public static final String DESCRIPTION_SORT_PARAM = "Campo por el cual ordenar los resultados";
    public static final String DESCRIPTION_DIRECTION_PARAM = "Dirección del ordenamiento (ASC o DESC)";
    
    public static final String DESCRIPTION_SUCCESS_RESPONSE = "Lista paginada de solicitudes de préstamo pendientes obtenida exitosamente";
    public static final String DESCRIPTION_BAD_REQUEST = "Parámetros de consulta inválidos";
    public static final String DESCRIPTION_INTERNAL_ERROR = "Error interno del servidor";
}