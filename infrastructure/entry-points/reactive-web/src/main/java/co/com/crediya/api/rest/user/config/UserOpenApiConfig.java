package co.com.crediya.api.rest.user.config;


import co.com.crediya.api.exception.ErrorResponse;
import co.com.crediya.api.rest.user.constant.UserEndpoint;
import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.dto.UserExistsResponseDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

public class UserOpenApiConfig {

    public static Consumer<Builder> createApplicantDocsConsumer() {
        return builder -> builder
                .summary(UserEndpoint.CREATE_APPLICANT.getSummary())
                .description(UserEndpoint.CREATE_APPLICANT.getDescription())
                .operationId(UserEndpoint.CREATE_APPLICANT.getOperationId())
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(CreateApplicantRequestDto.class)
                                )
                        )
                )
                .response(responseBuilder()
                        .responseCode(HttpStatus.CREATED.name())
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(UserResponseDto.class)
                                )
                        )
                )
                .response(responseBuilder()
                        .responseCode(HttpStatus.BAD_REQUEST.name())
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(ErrorResponse.class)
                                )
                        )
                );
    }

    public static Consumer<Builder> getByIdentityNumberDocsConsumer() {
        return ops -> ops
                .operationId(UserEndpoint.GET_BY_IDENTITY_NUMBER.getOperationId())
                .summary(UserEndpoint.GET_BY_IDENTITY_NUMBER.getSummary())
                .description(UserEndpoint.GET_BY_IDENTITY_NUMBER.getDescription())
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

    public static Consumer<Builder> existsByIdentityNumberDocsConsumer() {
        return ops -> ops
                .operationId(UserEndpoint.EXISTS_BY_IDENTITY_NUMBER.getOperationId())
                .summary(UserEndpoint.EXISTS_BY_IDENTITY_NUMBER.getSummary())
                .description(UserEndpoint.EXISTS_BY_IDENTITY_NUMBER.getDescription())
                .response(responseBuilder()
                        .responseCode(HttpStatus.OK.name())
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(UserExistsResponseDto.class)
                                )
                        )
                );
    }
}
