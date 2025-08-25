package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.api.rest.user.mapper.UserResponseMapperImpl;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseMapperTest {

    private final UserResponseMapperImpl mapper = new UserResponseMapperImpl();

    @Test
    void shouldMapUserToDtoSuccessfully() {
        // given
        Role role = new Role(1L, "APPLICANT", "Applicant role");
        User user = User.builder()
                .id(10L)
                .firstName("Juan")
                .lastName("Mendez")
                .identityNumber("123456")
                .phoneNumber("3001234567")
                .birthdate(LocalDate.of(2000, 1, 1))
                .address("Calle 123")
                .baseSalary(5000000L)
                .email("juan@test.com")
                .role(role)
                .build();

        UserResponseDto dto = mapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.firstName()).isEqualTo("Juan");
        assertThat(dto.lastName()).isEqualTo("Mendez");
        assertThat(dto.identityNumber()).isEqualTo("123456");
        assertThat(dto.phoneNumber()).isEqualTo("3001234567");
        assertThat(dto.birthdate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(dto.address()).isEqualTo("Calle 123");
        assertThat(dto.baseSalary()).isEqualTo(5000000L);
        assertThat(dto.email()).isEqualTo("juan@test.com");
        assertThat(dto.role()).isEqualTo(role);
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        UserResponseDto dto = mapper.toDto(null);

        assertThat(dto).isNull();
    }
}
