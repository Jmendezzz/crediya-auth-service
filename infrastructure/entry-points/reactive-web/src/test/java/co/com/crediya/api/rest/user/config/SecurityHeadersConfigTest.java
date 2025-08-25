package co.com.crediya.api.rest.user.config;

import co.com.crediya.api.config.SecurityHeadersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
public class SecurityHeadersConfigTest  {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> router = route()
                .GET("/test", request -> ServerResponse.ok().bodyValue("ok"))
                .build();

        webTestClient = WebTestClient.bindToRouterFunction(router)
                .webFilter(new SecurityHeadersConfig())
                .build();
    }

    @Test
    void shouldApplySecurityHeaders() {
        webTestClient.get().uri("/test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy", "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }
}
