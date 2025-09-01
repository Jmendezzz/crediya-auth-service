package co.com.crediya.api.rest.auth;

import co.com.crediya.api.rest.auth.constant.AuthHandlerLog;
import co.com.crediya.api.rest.auth.dto.LoginRequestDto;
import co.com.crediya.api.rest.auth.dto.LoginResponseDto;
import co.com.crediya.api.rest.auth.mapper.AuthRequestMapper;
import co.com.crediya.api.rest.auth.mapper.AuthResponseMapper;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.api.rest.user.mapper.UserResponseMapper;
import co.com.crediya.api.utils.ObjectValidator;
import co.com.crediya.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;
    private final AuthResponseMapper authResponseMapper;
    private final AuthRequestMapper authRequestMapper;
    private final ObjectValidator validator;
    private final UserResponseMapper userResponseMapper;

    public Mono<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        return validator.validate(loginRequestDto)
                .doOnSubscribe(s -> log.info(AuthHandlerLog.LOGIN_VALIDATION.getMessage()))
                .map(authRequestMapper::toDomain)
                .doOnNext(domain -> log.info(AuthHandlerLog.LOGIN_REQUEST.getMessage(), loginRequestDto.email()))
                .flatMap(authUseCase::login)
                .doOnSuccess(result -> log.info(AuthHandlerLog.LOGIN_SUCCESS.getMessage(), loginRequestDto.email()))
                .doOnError(error -> log.error(AuthHandlerLog.LOGIN_ERROR.getMessage(), loginRequestDto.email(), error.getMessage(), error))
                .map(authResponseMapper::toDto);
    }


    public Mono<UserResponseDto> me(){
        return authUseCase.me()
                .map(userResponseMapper::toDto);
    }
}