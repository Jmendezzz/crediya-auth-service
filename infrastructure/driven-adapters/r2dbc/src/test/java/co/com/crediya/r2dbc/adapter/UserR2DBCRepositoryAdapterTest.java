package co.com.crediya.r2dbc.adapter;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.mapper.RoleMapper;
import co.com.crediya.r2dbc.mapper.UserMapper;
import co.com.crediya.r2dbc.repository.RoleReactiveRepository;
import co.com.crediya.r2dbc.repository.UserReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserR2DBCRepositoryAdapterTest {

    private UserReactiveRepository userReactiveRepository;
    private RoleReactiveRepository roleReactiveRepository;
    private TransactionalOperator transactionalOperator;
    private UserMapper userMapper;
    private RoleMapper roleMapper;
    private UserR2DBCRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        userReactiveRepository = mock(UserReactiveRepository.class);
        roleReactiveRepository = mock(RoleReactiveRepository.class);
        transactionalOperator = mock(TransactionalOperator.class);
        userMapper = mock(UserMapper.class);
        roleMapper = mock(RoleMapper.class);

        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        adapter = new UserR2DBCRepositoryAdapter(
                userReactiveRepository,
                roleReactiveRepository,
                transactionalOperator,
                userMapper,
                roleMapper
        );
    }

    @Test
    void saveShouldPersistUserAndEnrichWithRole() {
        User user = User.builder()
                .id(1L)
                .firstName("Juan")
                .role(Role.builder().id(99L).name("ADMIN").build())
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setRoleId(99L);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(99L);
        roleEntity.setName("ADMIN");

        Role mappedRole = Role.builder().id(99L).name("ADMIN").build();
        User enrichedUser = User.builder().id(1L).firstName("Juan").role(mappedRole).build();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(userReactiveRepository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(roleReactiveRepository.findById(99L)).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toDomain(roleEntity)).thenReturn(mappedRole);
        when(userMapper.toDomain(userEntity, mappedRole)).thenReturn(enrichedUser);

        StepVerifier.create(adapter.save(user))
                .expectNext(enrichedUser)
                .verifyComplete();

        verify(userMapper).toEntity(user);
        verify(userReactiveRepository).save(userEntity);
        verify(roleReactiveRepository).findById(99L);
        verify(roleMapper).toDomain(roleEntity);
        verify(userMapper).toDomain(userEntity, mappedRole);
    }

    @Test
    void findByEmailShouldReturnUserWithRole() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setRoleId(88L);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(88L);
        roleEntity.setName(RoleConstant.APPLICANT);

        Role mappedRole = Role.builder().id(88L).name(RoleConstant.APPLICANT).build();
        User expectedUser = User.builder().id(2L).role(mappedRole).build();

        when(userReactiveRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(userEntity));
        when(roleReactiveRepository.findById(88L)).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toDomain(roleEntity)).thenReturn(mappedRole);
        when(userMapper.toDomain(userEntity, mappedRole)).thenReturn(expectedUser);

        StepVerifier.create(adapter.findByEmail("test@mail.com"))
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userReactiveRepository).findByEmail("test@mail.com");
        verify(roleReactiveRepository).findById(88L);
        verify(roleMapper).toDomain(roleEntity);
        verify(userMapper).toDomain(userEntity, mappedRole);
    }

    @Test
    void findByIdentityNumberShouldReturnUserWithRole() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(3L);
        userEntity.setRoleId(77L);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(77L);
        roleEntity.setName(RoleConstant.APPLICANT);

        Role mappedRole = Role.builder().id(77L).name(RoleConstant.APPLICANT).build();
        User expectedUser = User.builder().id(3L).role(mappedRole).build();

        when(userReactiveRepository.findByIdentityNumber("123")).thenReturn(Mono.just(userEntity));
        when(roleReactiveRepository.findById(77L)).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toDomain(roleEntity)).thenReturn(mappedRole);
        when(userMapper.toDomain(userEntity, mappedRole)).thenReturn(expectedUser);

        StepVerifier.create(adapter.findByIdentityNumber("123"))
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userReactiveRepository).findByIdentityNumber("123");
        verify(roleReactiveRepository).findById(77L);
        verify(roleMapper).toDomain(roleEntity);
        verify(userMapper).toDomain(userEntity, mappedRole);
    }
}
