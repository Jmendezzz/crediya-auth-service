package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.common.exceptions.DomainException;

public class UserEmailAlreadyExistsException extends DomainException {

    public UserEmailAlreadyExistsException(String email) {
        super(
                "El correo electrónico ya está registrado: " + email,
                "USER_EMAIL_ALREADY_EXISTS"
        );
    }
}
