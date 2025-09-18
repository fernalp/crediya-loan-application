package com.crediya.solicitudes.api.validator;

import com.crediya.solicitudes.model.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ReactiveValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        if (!violations.isEmpty()) {
            return Mono.error(new ValidationException(message));
        }
        return Mono.just(target);
    }
}
