package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRoute {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route()
                .path(UserEndpoint.BASE.getPath(), builder -> builder
                        .POST(UserEndpoint.CREATE_APPLICANT.getPath(), req ->
                                req.bodyToMono(CreateApplicantRequestDto.class)
                                        .flatMap(handler::createApplicant)
                                        .flatMap(dto -> ServerResponse
                                                .status(HttpStatus.CREATED)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(dto)
                                        )
                        ))
                .build();
    }
}
