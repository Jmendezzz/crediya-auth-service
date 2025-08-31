package co.com.crediya.model.auth.exceptions;

import co.com.crediya.model.common.exceptions.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Credenciales inválidas", "INVALID_CREDENTIALS");
    }
}
