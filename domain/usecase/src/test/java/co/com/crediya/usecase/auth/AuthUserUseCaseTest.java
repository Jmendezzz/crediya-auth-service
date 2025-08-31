package co.com.crediya.usecase.auth;

import co.com.crediya.model.auth.command.LoginCommand;
import co.com.crediya.model.auth.exceptions.InvalidCredentialsException;
import co.com.crediya.model.auth.gateways.PasswordMatcher;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.user.User;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordMatcher passwordMatcher;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .password("hashedPassword")
                .role(new Role(1L, RoleConstant.ADMINISTRATOR, "Administrador"))
                .build();
    }

    @Test
    void shouldLoginSuccessfullyWhenCredentialsAreValid() {
        LoginCommand command = new LoginCommand("juan@test.com", "plainPassword");

        when(userRepository.findByEmail(command.email())).thenReturn(Mono.just(user));
        when(passwordMatcher.matches(command.password(), user.getPassword())).thenReturn(Mono.just(true));
        when(tokenService.generateToken(any(User.class))).thenReturn("jwt-token");

        StepVerifier.create(authUseCase.login(command))
                .expectNextMatches(result -> result.token().equals("jwt-token"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenUserNotFound() {
        LoginCommand command = new LoginCommand("notfound@test.com", "password");

        when(userRepository.findByEmail(command.email())).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login(command))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    void shouldFailWhenPasswordDoesNotMatch() {
        LoginCommand command = new LoginCommand("juan@test.com", "wrongPassword");

        when(userRepository.findByEmail(command.email())).thenReturn(Mono.just(user));
        when(passwordMatcher.matches(command.password(), user.getPassword())).thenReturn(Mono.just(false));

        StepVerifier.create(authUseCase.login(command))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
}