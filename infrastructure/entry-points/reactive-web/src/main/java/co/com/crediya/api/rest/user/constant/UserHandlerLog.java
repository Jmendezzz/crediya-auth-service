package co.com.crediya.api.rest.user.constant;

import lombok.Getter;

@Getter
public enum UserHandlerLog {
    CREATE_VALIDATION("Validating CreateApplicantRequestDto"),
    CREATE_REQUEST("Starting createApplicant with request: {}"),
    CREATE_SUCCESS("User created successfully with id={}"),
    CREATE_ERROR("Error while creating user: {}"),

    FIND_BY_IDENTITY_NUMBER_REQUEST("Retrieving user with identityNumber={}"),
    FIND_BY_IDENTITY_NUMBER_ERROR("Error while getting user for identityNumber={}: {}"),


    EXISTS_REQUEST("Checking if user exists with identityNumber={}"),
    EXISTS_SUCCESS("User existence check completed for identityNumber={} result={}"),
    EXISTS_ERROR("Error while checking user existence for identityNumber={}: {}");

    private final String message;

    UserHandlerLog(String message) {
        this.message = message;
    }
}