package co.com.crediya.api.rest.user;

import co.com.crediya.api.rest.user.dto.CreateApplicantRequestDto;
import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class UserRouteTest {

    private WebTestClient webTestClient;
    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = mock(UserHandler.class);

        UserRoute userRoute = new UserRoute();
        this.webTestClient = WebTestClient.bindToRouterFunction(
                userRoute.userRoutes(userHandler)
        ).build();
    }

    @Test
    void shouldRouteToCreateApplicantSuccessfully() {
        CreateApplicantRequestDto requestDto = new CreateApplicantRequestDto(
                "Juan", "Mendez", "123456", "3001234567",
                LocalDate.of(1990, 1, 1), "Calle 123",
                5_000_000L, "juan@test.com"
        );

        User user = User.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Mendez")
                .identityNumber("123456")
                .phoneNumber("3001234567")
                .birthdate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .baseSalary(5_000_000L)
                .email("juan@test.com")
                .password("encoded")
                .role(new Role(2L, RoleConstant.APPLICANT, "Description"))
                .build();

        UserResponseDto responseDto = new UserResponseDto(
                1L, "Juan", "Mendez", "123456", "3001234567",
                LocalDate.of(1990, 1, 1), "Calle 123",
                5_000_000L, "juan@test.com", user.getRole()
        );

        when(userHandler.createApplicant(any())).thenReturn(
                Mono.just(ServerResponse
                        .created(null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseDto)
                        .block())
        );

        // when/then
        webTestClient.post()
                .uri("/api/v1/users/create-applicant")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseDto.class)
                .value(res -> {
                    org.assertj.core.api.Assertions.assertThat(res.id()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(res.firstName()).isEqualTo("Juan");
                });

        verify(userHandler, times(1)).createApplicant(any());
    }
}
