package com.crediya.solicitudes.usecase.exceptions;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationExceptionTest {

    @Test
    void shouldThrowCommunicationException() {
        String CODE = "COMMUNICATION_ERROR";
        String MESSAGE = "message";
        StepVerifier.create(Mono.error(new CommunicationException(MESSAGE)))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CommunicationException.class, error);
                    assertEquals(CODE, ((CommunicationException) error).getCode());
                    assertEquals(MESSAGE, error.getMessage());
                    assertTrue(error.toString().contains(MESSAGE));
                    assertTrue(error.toString().contains(CODE));
                }).verify();
    }

}