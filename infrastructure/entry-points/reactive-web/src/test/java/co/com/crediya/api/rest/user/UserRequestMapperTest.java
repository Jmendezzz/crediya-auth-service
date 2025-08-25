package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.mapper.UserRequestMapper;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserRequestMapperTest {

    private final UserRequestMapper mapper = Mappers.getMapper(UserRequestMapper.class);

    @Test
    void shouldMapCreateApplicantRequestDtoToUser() {
        CreateApplicantRequestDto dto = new CreateApplicantRequestDto(
                "Juan",
                "Pérez",
                "123456789",
                "3001234567",
                LocalDate.of(2000, 1, 1),
                "Calle 123",
                1_500_000L,
                "juan.perez@example.com"
        );

        User user = mapper.toDomain(dto);

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(dto.firstName());
        assertThat(user.getLastName()).isEqualTo(dto.lastName());
        assertThat(user.getIdentityNumber()).isEqualTo(dto.identityNumber());
        assertThat(user.getPhoneNumber()).isEqualTo(dto.phoneNumber());
        assertThat(user.getBirthdate()).isEqualTo(dto.birthdate());
        assertThat(user.getAddress()).isEqualTo(dto.address());
        assertThat(user.getBaseSalary()).isEqualTo(dto.baseSalary());
        assertThat(user.getEmail()).isEqualTo(dto.email());
        assertThat(user.getPassword()).isEqualTo(dto.identityNumber());
        assertThat(user.getRole()).isNull();
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        User user = mapper.toDomain(null);

        assertThat(user).isNull();
    }
}
