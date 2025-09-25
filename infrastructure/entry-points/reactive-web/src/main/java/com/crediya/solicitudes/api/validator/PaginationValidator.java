package com.crediya.solicitudes.api.validator;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.api.enums.SortDirection;
import com.crediya.solicitudes.api.enums.SortField;
import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationValidator {

    public static Mono<PageFilter> validateAndExtractPageFilter(ServerRequest serverRequest) {
        try {
            String pageParam = serverRequest.queryParam(ApiConstants.PARAMETER_PAGE).orElse(ApiConstants.DEFAULT_PAGE);
            String sizeParam = serverRequest.queryParam(ApiConstants.PARAMETER_SIZE).orElse(ApiConstants.DEFAULT_SIZE);
            String sortParam = serverRequest.queryParam(ApiConstants.PARAMETER_SORT).orElse(ApiConstants.DEFAULT_SORT);
            String directionParam = serverRequest.queryParam(ApiConstants.PARAMETER_DIRECTION).orElse(ApiConstants.DEFAULT_DIRECTION);

            int page = validatePage(pageParam);

            int size = validateSize(sizeParam);

            String direction = validateDirection(directionParam);

            String sort = validateSortField(sortParam);

            PageFilter pageFilter = PageFilter.builder()
                    .page(page)
                    .size(size)
                    .sort(sort)
                    .direction(direction)
                    .build();

            return Mono.just(pageFilter);

        } catch (ValidationException e) {
            return Mono.error(e);
        } catch (Exception e) {
            return Mono.error(new ValidationException("Error al validar parámetros de paginación: " + e.getMessage()));
        }
    }

    private static int validatePage(String pageParam) {
        try {
            int page = Integer.parseInt(pageParam);
            if (page < ApiConstants.MIN_PAGE) {
                throw new ValidationException(ApiConstants.ERROR_INVALID_PAGE);
            }
            if (page > ApiConstants.MAX_PAGE) {
                throw new ValidationException(ApiConstants.ERROR_PAGE_OUT_OF_BOUNDS);
            }
            return page;
        } catch (NumberFormatException e) {
            throw new ValidationException(ApiConstants.ERROR_INVALID_PAGE);
        }
    }

    private static int validateSize(String sizeParam) {
        try {
            int size = Integer.parseInt(sizeParam);
            if (size < ApiConstants.MIN_SIZE || size > ApiConstants.MAX_SIZE) {
                throw new ValidationException(ApiConstants.ERROR_INVALID_SIZE);
            }
            return size;
        } catch (NumberFormatException e) {
            throw new ValidationException(ApiConstants.ERROR_INVALID_SIZE);
        }
    }

    private static String validateDirection(String directionParam) {
        if (!SortDirection.isValidDirection(directionParam)) {
            throw new ValidationException(
                ApiConstants.ERROR_INVALID_DIRECTION + ". Valores permitidos: " + SortDirection.getValidDirectionsAsString()
            );
        }
        return SortDirection.fromValue(directionParam).getValue();
    }

    private static String validateSortField(String sortParam) {
        if (!SortField.isValidField(sortParam)) {
            throw new ValidationException(
                ApiConstants.ERROR_INVALID_SORT_FIELD + ". Campos permitidos: " + SortField.getValidFieldNamesAsString()
            );
        }
        return SortField.fromFieldName(sortParam).getFieldName();
    }
}