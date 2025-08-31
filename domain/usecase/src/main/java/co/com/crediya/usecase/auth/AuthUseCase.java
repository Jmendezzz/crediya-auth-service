package co.com.crediya.usecase.auth;

import co.com.crediya.model.auth.command.LoginCommand;
import co.com.crediya.model.auth.exceptions.InvalidCredentialsException;
import co.com.crediya.model.auth.gateways.PasswordMatcher;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.auth.result.LoginResult;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordMatcher passwordMatcher;
    private final TokenService tokenService;

    public Mono<LoginResult> login(LoginCommand command) {
        return userRepository.findByEmail(command.email())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
                .flatMap(user -> passwordMatcher.matches(command.password(), user.getPassword())
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
                        .map(valid -> new LoginResult(tokenService.generateToken(user)))
                );
    }

}