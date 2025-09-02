package co.com.crediya.config.seeder;

import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin().subscribe();
    }

    private Mono<User> seedAdmin() {
        final String defaultAdminEmail = "admin@crediya.com";
        final String defaultAdminIdentity = "0000000000";

        return userRepository.findByEmail(defaultAdminEmail)
                .switchIfEmpty(
                        roleRepository.findByName(RoleConstant.ADMINISTRATOR)
                                .switchIfEmpty(Mono.error(new IllegalStateException("Role ADMINISTRATOR not found")))
                                .flatMap(role -> passwordEncoder.encode("admin123")
                                        .map(encoded -> User.builder()
                                                .firstName("Default")
                                                .lastName("Admin")
                                                .email(defaultAdminEmail)
                                                .identityNumber(defaultAdminIdentity)
                                                .password(encoded)
                                                .role(role)
                                                .build()
                                        )
                                )
                                .flatMap(userRepository::save)
                                .doOnNext(user -> log.info("Created default ADMIN user with email: {}", user.getEmail()))
                )
                .doOnNext(user -> log.info("Verified ADMIN user: {}", user.getEmail()));
    }
}