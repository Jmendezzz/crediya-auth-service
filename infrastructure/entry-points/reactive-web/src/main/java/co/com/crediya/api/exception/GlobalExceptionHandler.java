package co.com.crediya.api.exception;

import co.com.crediya.model.common.exceptions.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Object body;

        if (ex instanceof DomainException domainException) {
            status = HttpStatus.BAD_REQUEST;
            body = Map.of(
                    "code", domainException.getCode(),
                    "message", domainException.getMessage()
            );
        }
        else if (ex instanceof ConstraintViolationException violationEx) {
            status = HttpStatus.BAD_REQUEST;
            List<Map<String, String>> errors = violationEx.getConstraintViolations().stream()
                    .map(v -> Map.of(
                            "field", v.getPropertyPath().toString(),
                            "message", v.getMessage()
                    ))
                    .toList();

            body = Map.of(
                    "code", "VALIDATION_ERROR",
                    "errors", errors
            );
        }
        else {
            body = Map.of(
                    "code", "INTERNAL_ERROR",
                    "message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error"
            );
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (Exception e) {
            bytes = ("{\"code\":\"SERIALIZATION_ERROR\",\"message\":\"Error serializing response\"}")
                    .getBytes(StandardCharsets.UTF_8);
        }

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}