package co.com.crediya.config;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UseCasesConfigTest {

    @Test
    void shouldCreateUserUseCaseBean() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.registerBean(UserRepository.class, () -> userRepository);
        context.registerBean(RoleRepository.class, () -> roleRepository);
        context.registerBean(PasswordEncoder.class, () -> passwordEncoder);
        context.register(UseCasesConfig.class);
        context.refresh();

        UserUseCase userUseCase = context.getBean(UserUseCase.class);

        assertThat(userUseCase).isNotNull();

        context.close();
    }
}