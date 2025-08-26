package co.com.crediya.api.rest.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserEndpoint {
    BASE("/api/v1/users"),
    CREATE_APPLICANT("/create-applicant");

    private final String path;

    public String fullPath() {
        return BASE.path + this.path;
    }
}