package co.com.crediya.config;

import co.com.crediya.model.auth.gateways.PasswordMatcher;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.auth.AuthUseCase;
import co.com.crediya.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.crediya.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public UserUseCase userUseCase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
                return new UserUseCase(
                        userRepository,
                        roleRepository,
                        passwordEncoder
                );
        }

        @Bean
        public AuthUseCase authUseCase(UserRepository userRepository, PasswordMatcher passwordMatcher, TokenService tokenService){
                return new AuthUseCase(
                        userRepository,
                        passwordMatcher,
                        tokenService
                );
        }
}

