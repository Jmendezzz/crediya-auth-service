package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.constant.UserHandlerLog;
import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.dto.UserExistsResponseDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.api.rest.user.mapper.UserRequestMapper;
import co.com.crediya.api.rest.user.mapper.UserResponseMapper;
import co.com.crediya.api.utils.ObjectValidator;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserRequestMapper requestMapper;
    private final UserResponseMapper responseMapper;
    private final ObjectValidator validator;

    public Mono<UserResponseDto> createApplicant(CreateApplicantRequestDto dto) {
        return validator.validate(dto)
                .doOnSubscribe(s -> log.info(UserHandlerLog.CREATE_VALIDATION.getMessage()))
                .map(requestMapper::toDomain)
                .doOnNext(domain -> log.info(UserHandlerLog.CREATE_REQUEST.getMessage(), dto))
                .flatMap(userUseCase::createApplicant)
                .doOnSuccess(user -> log.info(UserHandlerLog.CREATE_SUCCESS.getMessage(), user.getId()))
                .doOnError(error -> log.error(UserHandlerLog.CREATE_ERROR.getMessage(), error.getMessage(), error))
                .map(responseMapper::toDto);
    }

    public Mono<UserResponseDto> getByIdentityNumber(String identityNumber) {
        return userUseCase.getByIdentityNumber(identityNumber)
                .doOnSubscribe(s -> log.info(UserHandlerLog.FIND_BY_IDENTITY_NUMBER_REQUEST.getMessage(), identityNumber))
                .doOnError(error -> log.error(UserHandlerLog.FIND_BY_IDENTITY_NUMBER_ERROR.getMessage(), identityNumber, error.getMessage(), error))
                .map(responseMapper::toDto);

    }

    public Mono<UserExistsResponseDto> existsByIdentityNumber(String identityNumber) {
        log.info(UserHandlerLog.EXISTS_REQUEST.getMessage(), identityNumber);

        return userUseCase.existsByIdentityNumber(identityNumber)
                .map(exists -> {
                    log.info(UserHandlerLog.EXISTS_SUCCESS.getMessage(), identityNumber, exists);
                    return new UserExistsResponseDto(exists);
                })
                .doOnError(error -> log.error(UserHandlerLog.EXISTS_ERROR.getMessage(), identityNumber, error.getMessage(), error));
    }
}