package co.com.crediya.api.rest.user.exception;


import co.com.crediya.api.exception.GlobalExceptionHandler;
import co.com.crediya.model.common.exceptions.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(new ObjectMapper());
    }

    @Test
    void shouldHandleDomainException() {
        DomainException ex = new DomainException("CODE_X", "Domain message") {};
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
        );
        Mono<Void> result = handler.handle(exchange, ex);

        StepVerifier.create(result).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getResponse().getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path mockPath = Mockito.mock(Path.class);
        Mockito.when(mockPath.toString()).thenReturn("fieldX");
        Mockito.when(violation.getPropertyPath()).thenReturn(mockPath);
        Mockito.when(violation.getMessage()).thenReturn("must not be null");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
        );
        Mono<Void> result = handler.handle(exchange, ex);

        StepVerifier.create(result).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("boom!");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
        );

        Mono<Void> result = handler.handle(exchange, ex);

        StepVerifier.create(result).verifyComplete();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}