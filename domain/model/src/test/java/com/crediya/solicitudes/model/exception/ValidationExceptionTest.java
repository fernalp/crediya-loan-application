package com.crediya.solicitudes.model.exception;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void shouldThrowValidationException() {
        String CODE = "VALIDATION_ERROR";
        String MESSAGE = "message";
        StepVerifier.create(Mono.error(new ValidationException(MESSAGE)))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(ValidationException.class, error);
                    assertEquals(CODE, ((ValidationException) error).getCode());
                    assertEquals(MESSAGE, error.getMessage());
                    assertTrue(error.toString().contains(MESSAGE));
                    assertTrue(error.toString().contains(CODE));
                }).verify();
    }

}