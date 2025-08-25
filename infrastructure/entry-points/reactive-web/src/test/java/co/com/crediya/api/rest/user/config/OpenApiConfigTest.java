package co.com.crediya.api.rest.user.config;

import co.com.crediya.api.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void crediyaOpenApiShouldReturnConfiguredOpenAPI() {
        OpenAPI openAPI = config.crediyaOpenAPI();

        assertThat(openAPI).isNotNull();
        Info info = openAPI.getInfo();
        assertThat(info.getTitle()).isEqualTo("CrediYa Auth Service API");
        assertThat(info.getVersion()).isEqualTo("v1.0");
        assertThat(info.getDescription()).contains("autenticación");
        assertThat(info.getContact().getEmail()).isEqualTo("backend@crediya.com");
    }

    @Test
    void userApiShouldReturnGroupedOpenApi() {
        GroupedOpenApi groupedOpenApi = config.userApi();

        assertThat(groupedOpenApi).isNotNull();
        assertThat(groupedOpenApi.getGroup()).isEqualTo("Usuarios");
        assertThat(groupedOpenApi.getPathsToMatch()).containsExactly("/api/v1/users/**");
    }
}