package co.com.crediya.api.rest.auth;

import co.com.crediya.api.rest.auth.config.AuthOpenApiConfig;
import co.com.crediya.api.rest.auth.constant.AuthEndpoint;
import co.com.crediya.api.rest.auth.dto.LoginRequestDto;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRoute {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return SpringdocRouteBuilder.route()
                .POST(AuthEndpoint.LOGIN.getPath(),
                        req -> req.bodyToMono(LoginRequestDto.class)
                                .flatMap(handler::login)
                                .flatMap(dto ->
                                        ServerResponse
                                                .status(HttpStatus.OK)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(dto)
                                ),
                        ops -> AuthOpenApiConfig.loginDocsConsumer().accept(ops)
                )
                .GET(AuthEndpoint.ME.getPath(),
                        req -> handler.me()
                                .flatMap(dto ->
                                        ServerResponse
                                                .status(HttpStatus.OK)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(dto)
                                ),
                        ops -> AuthOpenApiConfig.meDocsConsumer().accept(ops)
                        )
                .build();
    }

}
