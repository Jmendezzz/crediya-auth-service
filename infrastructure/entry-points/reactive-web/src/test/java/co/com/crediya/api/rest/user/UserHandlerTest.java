package co.com.crediya.api.rest.user;


import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.api.rest.user.mapper.UserRequestMapper;
import co.com.crediya.api.rest.user.mapper.UserResponseMapper;
import co.com.crediya.api.utils.ObjectValidator;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.user.UserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    private UserUseCase userUseCase;
    private UserRequestMapper requestMapper;
    private UserResponseMapper responseMapper;
    private ObjectValidator validator;
    private UserHandler userHandler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userUseCase = mock(UserUseCase.class);
        requestMapper = mock(UserRequestMapper.class);
        responseMapper = mock(UserResponseMapper.class);
        validator = new ObjectValidator(Validation.buildDefaultValidatorFactory().getValidator());
        userHandler = new UserHandler(userUseCase, requestMapper, responseMapper, validator);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);    }


    private ServerRequest buildRequest(Object body) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(body);
        DataBuffer buffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());
        return ServerRequest.create(
                MockServerWebExchange.from(
                        MockServerHttpRequest.post("/api/v1/users/create-applicant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Flux.just(buffer))
                ),
                HandlerStrategies.withDefaults().messageReaders()
        );
    }

    @Test
    void shouldCreateApplicantSuccessfully() {
        CreateApplicantRequestDto requestDto = new CreateApplicantRequestDto(
                "Juan", "Mendez", "123456", "3001234567",
                LocalDate.now().minusYears(20), "Calle 123",
                5_000_000L, "juan@test.com"
        );

        User user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Mendez")
                .identityNumber("123456")
                .phoneNumber("3001234567")
                .birthdate(LocalDate.now().minusYears(20))
                .address("Calle 123")
                .baseSalary(5_000_000L)
                .email("juan@test.com")
                .password("encoded")
                .role(new Role(2L, RoleConstant.APPLICANT, "Description"))
                .build();

        UserResponseDto responseDto = new UserResponseDto(
                1L, "Juan", "Mendez", "123456", "3001234567",
                LocalDate.now().plusYears(1), "Calle 123",
                5_000_000L, "juan@test.com", user.getRole()
        );

        when(requestMapper.toDomain(any(CreateApplicantRequestDto.class))).thenReturn(user);
        when(userUseCase.createApplicant(any(User.class))).thenReturn(Mono.just(user));
        when(responseMapper.toDto(any(User.class))).thenReturn(responseDto);

        Mono<UserResponseDto> result = userHandler.createApplicant(requestDto);

        StepVerifier.create(result)
                .expectNext(responseDto)
                .verifyComplete();

        verify(userUseCase, times(1)).createApplicant(any(User.class));
    }

    @Test
    void shouldFailValidationWhenMissingFields() {
        CreateApplicantRequestDto invalidDto = new CreateApplicantRequestDto(
                "", "", "", "", LocalDate.now().minusYears(1),
                "", -10L, "bad-email"
        );

        Mono<UserResponseDto> result = userHandler.createApplicant(invalidDto);

        StepVerifier.create(result)
                .expectError(jakarta.validation.ConstraintViolationException.class)
                .verify();
    }

}