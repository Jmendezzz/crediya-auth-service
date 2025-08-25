package co.com.crediya.api.rest.user.dto;

import co.com.crediya.model.role.Role;

import java.time.LocalDate;

public record UserResponseDto (
        Long id,
        String firstName,
        String lastName,
        String identityNumber,
        String phoneNumber,
        LocalDate birthdate,
        String address,
        Long baseSalary,
        String email,
        Role role
) {
}
