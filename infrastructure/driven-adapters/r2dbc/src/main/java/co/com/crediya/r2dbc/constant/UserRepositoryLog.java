package co.com.crediya.r2dbc.constant;

import lombok.Getter;

@Getter
public enum UserRepositoryLog {
    SAVE_START("Saving user with identityNumber={}"),
    SAVE_SUCCESS("User saved successfully with id={}"),
    SAVE_ERROR("Error saving user {}: {}"),

    FIND_BY_EMAIL("Finding user by email={}"),
    FIND_BY_EMAIL_SUCCESS("User found by email={}"),
    FIND_BY_EMAIL_NOT_FOUND("No user found by email={}"),
    FIND_BY_EMAIL_ERROR("Error finding user by email={}: {}"),

    FIND_BY_IDENTITY("Finding user by identityNumber={}"),
    FIND_BY_IDENTITY_SUCCESS("User found by identityNumber={}"),
    FIND_BY_IDENTITY_NOT_FOUND("No user found by identityNumber={}"),
    FIND_BY_IDENTITY_ERROR("Error finding user by identityNumber={}: {}"),

    EXISTS_BY_IDENTITY("Checking existence of user with identityNumber={}"),
    EXISTS_BY_IDENTITY_RESULT("User existence for identityNumber={} result={}"),

    ENRICH_START("Enriching UserEntity {} with roleId={}"),
    ENRICH_SUCCESS("UserEntity {} enriched successfully with role={}"),
    ENRICH_ERROR("Error enriching UserEntity {}: {}");

    private final String message;

    UserRepositoryLog(String message) {
        this.message = message;
    }
}