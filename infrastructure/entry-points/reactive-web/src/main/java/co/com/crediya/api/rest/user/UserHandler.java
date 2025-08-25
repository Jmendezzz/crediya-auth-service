package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.mapper.UserRequestMapper;
import co.com.crediya.api.rest.user.mapper.UserResponseMapper;
import co.com.crediya.api.utils.ObjectValidator;
import co.com.crediya.usecase.user.UserUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserRequestMapper requestMapper;
    private final UserResponseMapper responseMapper;
    private final ObjectValidator validator;

    public Mono<ServerResponse> createApplicant(ServerRequest request) {
        return request.bodyToMono(CreateApplicantRequestDto.class)
                .doOnNext(dto -> log.info(dto.toString()))
                .flatMap(validator::validate)
                .map(requestMapper::toDomain)
                .flatMap(userUseCase::createApplicant)
                .map(responseMapper::toDto)
                .flatMap(userCreatedDto ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userCreatedDto)
                );
    }
}