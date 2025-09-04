package co.com.crediya.usecase.user;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.role.exceptions.RoleNotFoundException;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.exceptions.UserEmailAlreadyExistsException;
import co.com.crediya.model.user.exceptions.UserIdentityNumberAlreadyExistsException;
import co.com.crediya.model.user.exceptions.UserNotFoundException;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .identityNumber("123")
                .email("juan@test.com")
                .password("plainPassword")
                .baseSalary(1_000_000L)
                .build();
    }

    @Test
    void shouldCreateApplicantSuccessfully() {
        Role applicantRole = new Role(1L, RoleConstant.APPLICANT, "Solicitante");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByIdentityNumber(user.getIdentityNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByName(RoleConstant.APPLICANT)).thenReturn(Mono.just(applicantRole));
        when(passwordEncoder.encode(user.getPassword())).thenReturn(Mono.just("hashedPassword"));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userUseCase.createApplicant(user))
                .expectNextMatches(saved ->
                        saved.getRole().getName().equals(RoleConstant.APPLICANT) &&
                                saved.getPassword().equals("hashedPassword") &&
                                saved.getEmail().equals(user.getEmail()))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
        when(userRepository.findByIdentityNumber(user.getIdentityNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByName(RoleConstant.APPLICANT)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createApplicant(user))
                .expectErrorMatches(ex -> ex instanceof UserEmailAlreadyExistsException &&
                        ex.getMessage().contains(user.getEmail()))
                .verify();
    }

    @Test
    void shouldFailWhenIdentityNumberAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByIdentityNumber(user.getIdentityNumber())).thenReturn(Mono.just(user));
        when(roleRepository.findByName(RoleConstant.APPLICANT)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createApplicant(user))
                .expectErrorMatches(ex -> ex instanceof UserIdentityNumberAlreadyExistsException &&
                        ex.getMessage().contains(user.getIdentityNumber()))
                .verify();
    }

    @Test
    void shouldFailWhenRoleNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByIdentityNumber(user.getIdentityNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByName(RoleConstant.APPLICANT)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createApplicant(user))
                .expectError(RoleNotFoundException.class)
                .verify();
    }


    @Test
    void shouldReturnTrueWhenUserWithIdentityNumberExists() {
        when(userRepository.findByIdentityNumber(user.getIdentityNumber()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.existsByIdentityNumber(user.getIdentityNumber()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenUserWithIdentityNumberDoesNotExist() {
        when(userRepository.findByIdentityNumber(user.getIdentityNumber()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.existsByIdentityNumber(user.getIdentityNumber()))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldReturnUserWhenIdentityNumberExists() {
        when(userRepository.findByIdentityNumber(user.getIdentityNumber()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.getByIdentityNumber(user.getIdentityNumber()))
                .expectNextMatches(found ->
                        found.getIdentityNumber().equals(user.getIdentityNumber()) &&
                                found.getFirstName().equals("Juan"))
                .verifyComplete();

        verify(userRepository).findByIdentityNumber(user.getIdentityNumber());
    }

    @Test
    void shouldThrowExceptionWhenIdentityNumberDoesNotExist() {
        when(userRepository.findByIdentityNumber(user.getIdentityNumber()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getByIdentityNumber(user.getIdentityNumber()))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(userRepository).findByIdentityNumber(user.getIdentityNumber());
    }

    
}