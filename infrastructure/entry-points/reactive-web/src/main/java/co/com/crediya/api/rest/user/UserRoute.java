package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRoute {

    private final String ENDPOINT_PREFFIX = "/api/v1/users";

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users/create-applicant",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "createApplicant",
                    operation = @Operation(
                            operationId = "createApplicant",
                            summary = "Crear solicitante",
                            description = "Registra un nuevo solicitante en el sistema",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreateApplicantRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Solicitante creado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route()
                .path(ENDPOINT_PREFFIX, builder -> builder
                        .POST("/create-applicant", handler::createApplicant))
                .build();
    }
}
