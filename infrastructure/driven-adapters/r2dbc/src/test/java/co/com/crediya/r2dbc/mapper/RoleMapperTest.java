package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperImplTest {

    private RoleMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new RoleMapperImpl();
    }

    @Test
    void toDomainShouldMapAllFields() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("ADMIN");
        entity.setDescription("Administrator role");

        Role role = mapper.toDomain(entity);

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo("ADMIN");
        assertThat(role.getDescription()).isEqualTo("Administrator role");
    }

    @Test
    void toDomainShouldReturnNullWhenEntityIsNull() {
        Role role = mapper.toDomain(null);
        assertThat(role).isNull();
    }

    @Test
    void toEntityShouldMapAllFields() {
        Role role = Role.builder()
                .id(2L)
                .name("APPLICANT")
                .description("Applicant role")
                .build();

        RoleEntity entity = mapper.toEntity(role);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getName()).isEqualTo("APPLICANT");
        assertThat(entity.getDescription()).isEqualTo("Applicant role");
    }

    @Test
    void toEntityShouldReturnNullWhenRoleIsNull() {
        RoleEntity entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }
}