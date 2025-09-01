package co.com.crediya.config;

import co.com.crediya.model.auth.gateways.AuthContext;
import co.com.crediya.model.auth.gateways.PasswordMatcher;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.auth.AuthUseCase;
import co.com.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UseCasesConfigTest {

    @Test
    void shouldCreateUserAndAuthUseCasesBeans() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        PasswordMatcher passwordMatcher = Mockito.mock(PasswordMatcher.class);
        TokenService tokenService = Mockito.mock(TokenService.class);
        AuthContext authContext = Mockito.mock(AuthContext.class);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(UserRepository.class, () -> userRepository);
        context.registerBean(RoleRepository.class, () -> roleRepository);
        context.registerBean(PasswordEncoder.class, () -> passwordEncoder);
        context.registerBean(PasswordMatcher.class, () -> passwordMatcher);
        context.registerBean(TokenService.class, () -> tokenService);
        context.registerBean(AuthContext.class, () -> authContext);
        context.register(UseCasesConfig.class);
        context.refresh();

        UserUseCase userUseCase = context.getBean(UserUseCase.class);
        AuthUseCase authUseCase = context.getBean(AuthUseCase.class);

        assertThat(userUseCase).isNotNull();
        assertThat(authUseCase).isNotNull();

        context.close();
    }
}