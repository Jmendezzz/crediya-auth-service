package co.com.crediya.model.user.exceptions;

import co.com.crediya.model.common.exceptions.DomainException;

public class UserIdentityNumberAlreadyExistsException extends DomainException {


    public UserIdentityNumberAlreadyExistsException(String identityNumber) {
        super(
                "El numero de identidad ya está registrado: " +  identityNumber,
                "USER_IDENTITY_NUMBER_ALREADY_EXISTS"
        );
    }
}
