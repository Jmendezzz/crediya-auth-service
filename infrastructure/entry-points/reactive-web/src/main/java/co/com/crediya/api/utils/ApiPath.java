package co.com.crediya.api.utils;

import co.com.crediya.api.constant.ApiResource;
import co.com.crediya.api.constant.ApiVersion;

public final class ApiPath {

    private ApiPath() {}

    public static String path(ApiVersion version, ApiResource resource, String endpoint) {
        return version.getPrefix() + resource.getResource() + endpoint;
    }
}