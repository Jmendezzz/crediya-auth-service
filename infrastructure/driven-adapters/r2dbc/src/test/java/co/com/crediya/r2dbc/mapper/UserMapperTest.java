package co.com.crediya.r2dbc.mapper;


import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    void toEntityShouldMapAllFieldsWhenUserHasRole() {
        Role role = new Role(2L, "APPLICANT", "Description");

        User user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Mendez")
                .identityNumber("123456")
                .phoneNumber("3001234567")
                .birthdate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .baseSalary(5_000_000L)
                .email("juan@test.com")
                .password("secret")
                .role(role)
                .build();

        UserEntity entity = mapper.toEntity(user);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).isEqualTo("Juan");
        assertThat(entity.getLastName()).isEqualTo("Mendez");
        assertThat(entity.getIdentityNumber()).isEqualTo("123456");
        assertThat(entity.getPhoneNumber()).isEqualTo("3001234567");
        assertThat(entity.getBirthdate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(entity.getAddress()).isEqualTo("Calle 123");
        assertThat(entity.getBaseSalary()).isEqualTo(5_000_000L);
        assertThat(entity.getEmail()).isEqualTo("juan@test.com");
        assertThat(entity.getPassword()).isEqualTo("secret");
        assertThat(entity.getRoleId()).isEqualTo(2L);
    }

    @Test
    void toEntityShouldReturnNullWhenUserIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toEntityShouldSetRoleIdNullWhenUserHasNoRole() {
        User user = User.builder()
                .id(99L)
                .firstName("Pepe")
                .build();

        UserEntity entity = mapper.toEntity(user);

        assertThat(entity.getRoleId()).isNull();
        assertThat(entity.getId()).isEqualTo(99L);
        assertThat(entity.getFirstName()).isEqualTo("Pepe");
    }

    @Test
    void toDomainShouldMapAllFields_henEntityAndRoleProvided() {
        UserEntity entity = new UserEntity();
        entity.setId(10L);
        entity.setFirstName("Ana");
        entity.setLastName("Lopez");
        entity.setIdentityNumber("987654");
        entity.setPhoneNumber("3112223333");
        entity.setBirthdate(LocalDate.of(2000, 5, 20));
        entity.setAddress("Carrera 45");
        entity.setBaseSalary(3_500_000L);
        entity.setEmail("ana@test.com");
        entity.setPassword("encoded");

        Role role = new Role(5L, "ADMIN", "Administrator role");

        User user = mapper.toDomain(entity, role);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getFirstName()).isEqualTo("Ana");
        assertThat(user.getLastName()).isEqualTo("Lopez");
        assertThat(user.getIdentityNumber()).isEqualTo("987654");
        assertThat(user.getPhoneNumber()).isEqualTo("3112223333");
        assertThat(user.getBirthdate()).isEqualTo(LocalDate.of(2000, 5, 20));
        assertThat(user.getAddress()).isEqualTo("Carrera 45");
        assertThat(user.getBaseSalary()).isEqualTo(3_500_000L);
        assertThat(user.getEmail()).isEqualTo("ana@test.com");
        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    void toDomain_ShouldReturnNull_WhenEntityAndRoleAreNull() {
        assertThat(mapper.toDomain(null, null)).isNull();
    }

    @Test
    void toDomainShouldMapOnlyRoleWhenEntityIsNull() {
        Role role = new Role(7L, "SUPPORT", "Support staff");

        User user = mapper.toDomain(null, role);

        assertThat(user).isNotNull();
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.getId()).isNull();
        assertThat(user.getFirstName()).isNull();
    }

    @Test
    void toDomainShouldMapOnlyEntityWhenRoleIsNull() {
        UserEntity entity = new UserEntity();
        entity.setId(11L);
        entity.setFirstName("Carlos");

        User user = mapper.toDomain(entity, null);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(11L);
        assertThat(user.getFirstName()).isEqualTo("Carlos");
        assertThat(user.getRole()).isNull();
    }
}