package co.com.crediya.model.role.exceptions;

import co.com.crediya.model.common.exceptions.DomainException;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(String roleName) {
        super(
                "No se encontró el rol con nombre: " + roleName,
                "ROLE_NOT_FOUND"
                );
    }
}
