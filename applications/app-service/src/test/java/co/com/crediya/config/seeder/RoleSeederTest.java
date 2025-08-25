package co.com.crediya.config.seeder;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RoleSeederTest {

    private RoleRepository roleRepository;
    private RoleSeeder roleSeeder;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        roleSeeder = new RoleSeeder(roleRepository);
    }

    @Test
    void seedRolesShouldInsertRolesIfNotExist() {
        when(roleRepository.findByName(RoleConstant.ADMINISTRATOR)).thenReturn(Mono.empty());
        when(roleRepository.findByName(RoleConstant.APPLICANT)).thenReturn(Mono.empty());

        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(roleSeeder.seedRoles())
                .expectNextMatches(role -> RoleConstant.ADMINISTRATOR.equals(role.getName()))
                .expectNextMatches(role -> RoleConstant.APPLICANT.equals(role.getName()))
                .verifyComplete();

        verify(roleRepository, times(1)).findByName(RoleConstant.ADMINISTRATOR);
        verify(roleRepository, times(1)).findByName(RoleConstant.APPLICANT);
        verify(roleRepository, times(2)).save(any(Role.class));
    }

    @Test
    void seedRolesShouldInsertIfRolesNotExist() {
        when(roleRepository.findByName(anyString())).thenReturn(Mono.empty());
        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(roleSeeder.seedRoles())
                .expectNextMatches(r -> RoleConstant.ADMINISTRATOR.equals(r.getName()))
                .expectNextMatches(r -> RoleConstant.APPLICANT.equals(r.getName()))
                .verifyComplete();

        verify(roleRepository, times(2)).save(any(Role.class));
    }

}