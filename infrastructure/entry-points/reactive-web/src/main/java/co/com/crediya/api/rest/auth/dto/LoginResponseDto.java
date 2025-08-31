package co.com.crediya.api.rest.auth.dto;

import co.com.crediya.api.rest.auth.constant.AuthOpenApiSchema;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(

        @Schema(description = AuthOpenApiSchema.TOKEN_DESCRIPTION, example = AuthOpenApiSchema.TOKEN_EXAMPLE)
        String token
) {
}
