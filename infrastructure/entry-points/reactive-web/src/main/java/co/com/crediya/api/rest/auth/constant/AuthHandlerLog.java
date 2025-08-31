package co.com.crediya.api.rest.auth.constant;

import lombok.Getter;

@Getter
public enum AuthHandlerLog {
    LOGIN_VALIDATION("Validating LoginRequestDto"),
    LOGIN_REQUEST("Starting login for email={}"),
    LOGIN_SUCCESS("Login successful for email={}"),
    LOGIN_ERROR("Error during login for email={}: {}");

    private final String message;

    AuthHandlerLog(String message) {
        this.message = message;
    }
}