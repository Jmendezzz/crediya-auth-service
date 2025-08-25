package co.com.crediya.passwordencoder;

import co.com.crediya.model.user.gateways.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordEncoderAdapterTest {

    private PasswordEncoder passwordEncoder;
    private BCryptPasswordEncoder rawEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoderAdapter();
        rawEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldEncodePasswordSuccessfully() {
        String rawPassword = "mySecret123";

        StepVerifier.create(passwordEncoder.encode(rawPassword))
                .assertNext(encoded -> {
                    assertThat(encoded).isNotBlank();
                    assertThat(rawEncoder.matches(rawPassword, encoded))
                            .as("Password debe coincidir con el hash BCrypt")
                            .isTrue();
                })
                .verifyComplete();
    }

    @Test
    void shouldProduceDifferentHashesForSamePassword() {
        String rawPassword = "samePassword";

        StepVerifier.create(passwordEncoder.encode(rawPassword))
                .assertNext(firstHash -> {
                    StepVerifier.create(passwordEncoder.encode(rawPassword))
                            .assertNext(secondHash -> {
                                assertThat(firstHash).isNotEqualTo(secondHash);
                                assertThat(rawEncoder.matches(rawPassword, secondHash)).isTrue();
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void shouldFailWhenPasswordIsNull() {
        String rawPassword = null;

        StepVerifier.create(passwordEncoder.encode(rawPassword))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}