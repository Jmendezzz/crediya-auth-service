package co.com.crediya.api.rest.auth.dto;

import co.com.crediya.api.rest.auth.constant.AuthOpenApiSchema;
import co.com.crediya.api.rest.auth.constant.AuthValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @Schema(description = AuthOpenApiSchema.EMAIL_DESCRIPTION, example = AuthOpenApiSchema.EMAIL_EXAMPLE)
        @NotBlank(message = AuthValidationMessage.EMAIL_REQUIRED)
        @Email(message = AuthValidationMessage.EMAIL_INVALID )
        String email,

        @Schema(description = AuthOpenApiSchema.PASSWORD_DESCRIPTION, example = AuthOpenApiSchema.PASSWORD_EXAMPLE)
        @NotBlank(message = AuthValidationMessage.PASSWORD_REQUIRED)
        String password
) {
}
