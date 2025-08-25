package co.com.crediya.api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crediyaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa Auth Service API")
                        .description("API para autenticación y gestión de usuarios solicitantes")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@crediya.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación completa del proyecto")
                        .url("https://tu-repo.gitlab.com/crediya/docs"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("Usuarios")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }
}