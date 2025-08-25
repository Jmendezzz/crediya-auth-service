package co.com.crediya.r2dbc.adapter;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.mapper.RoleMapper;
import co.com.crediya.r2dbc.repository.RoleReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RoleR2DBCRepositoryAdapterTest {

    private RoleReactiveRepository roleReactiveRepository;
    private RoleMapper roleMapper;
    private RoleR2DBCRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        roleReactiveRepository = mock(RoleReactiveRepository.class);
        roleMapper = mock(RoleMapper.class);
        adapter = new RoleR2DBCRepositoryAdapter(roleReactiveRepository, roleMapper);
    }

    @Test
    void findByNameShouldReturnMappedRole() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("ADMIN");
        entity.setDescription("Admin role");

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Admin role")
                .build();

        when(roleReactiveRepository.findByName("ADMIN")).thenReturn(Mono.just(entity));
        when(roleMapper.toDomain(entity)).thenReturn(role);

        StepVerifier.create(adapter.findByName("ADMIN"))
                .expectNext(role)
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findByName("ADMIN");
        verify(roleMapper, times(1)).toDomain(entity);
    }

    @Test
    void findAllShouldReturnMappedRoles() {
        RoleEntity entity1 = new RoleEntity();
        entity1.setId(1L);
        entity1.setName("ADMIN");
        entity1.setDescription("Admin role");

        RoleEntity entity2 = new RoleEntity();
        entity2.setId(2L);
        entity2.setName("APPLICANT");
        entity2.setDescription("Applicant role");

        Role role1 = Role.builder().id(1L).name("ADMIN").description("Admin role").build();
        Role role2 = Role.builder().id(2L).name("APPLICANT").description("Applicant role").build();

        when(roleReactiveRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(roleMapper.toDomain(entity1)).thenReturn(role1);
        when(roleMapper.toDomain(entity2)).thenReturn(role2);

        StepVerifier.create(adapter.findAll())
                .expectNext(role1, role2)
                .verifyComplete();

        verify(roleReactiveRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toDomain(entity1);
        verify(roleMapper, times(1)).toDomain(entity2);
    }

    @Test
    void saveShouldReturnMappedRole() {
        Role role = Role.builder()
                .id(3L)
                .name("USER")
                .description("User role")
                .build();

        RoleEntity entity = new RoleEntity();
        entity.setId(3L);
        entity.setName("USER");
        entity.setDescription("User role");

        when(roleMapper.toEntity(role)).thenReturn(entity);
        when(roleReactiveRepository.save(entity)).thenReturn(Mono.just(entity));
        when(roleMapper.toDomain(entity)).thenReturn(role);

        StepVerifier.create(adapter.save(role))
                .expectNext(role)
                .verifyComplete();

        verify(roleMapper, times(1)).toEntity(role);
        verify(roleReactiveRepository, times(1)).save(entity);
        verify(roleMapper, times(1)).toDomain(entity);
    }
}