package co.com.crediya.api.rest.auth.constant;

import co.com.crediya.api.constant.ApiResource;
import co.com.crediya.api.constant.ApiVersion;
import lombok.Getter;


@Getter
public enum AuthEndpoint {

    LOGIN(
            "/login",
            "login",
            "Iniciar Sesión",
            "Permite el inicio de sesión en el sistema"
    ),
    ME(
            "/me",
            "me",
            "Mis detalles",
            "Obtiene los detalles del usuario actualmente logueado"
    );

    private final String path;
    private final String operationId;
    private final String summary;
    private final String description;

    AuthEndpoint(String path, String operationId, String summary, String description) {
        this.path = ApiVersion.V1.getPrefix() + ApiResource.AUTH.getResource() + path;
        this.operationId = operationId;
        this.summary = summary;
        this.description = description;
    }
}
