package co.com.crediya.api.rest.auth.config;

import co.com.crediya.api.rest.auth.constant.AuthEndpoint;
import co.com.crediya.api.rest.auth.dto.LoginResponseDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class AuthOpenApiConfig {

    public static Consumer<Builder> loginDocsConsumer() {
        return ops -> ops
                .operationId(AuthEndpoint.LOGIN.getOperationId())
                .summary(AuthEndpoint.LOGIN.getSummary())
                .description(AuthEndpoint.LOGIN.getDescription())
                .response(responseBuilder()
                        .responseCode(HttpStatus.OK.name())
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(LoginResponseDto.class)
                                )
                        )
                );
    }

    public static Consumer<Builder> meDocsConsumer() {
        return ops -> ops
                .operationId(AuthEndpoint.ME.getOperationId())
                .summary(AuthEndpoint.ME.getSummary())
                .description(AuthEndpoint.ME.getDescription())
                .response(responseBuilder()
                        .responseCode(HttpStatus.OK.name())
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(UserResponseDto.class)
                                )
                        )
                );
    }
}
