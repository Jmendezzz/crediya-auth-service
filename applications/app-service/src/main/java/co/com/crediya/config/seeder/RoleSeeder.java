package co.com.crediya.config.seeder;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = List.of(
                RoleConstant.ADMINISTRATOR,
                RoleConstant.APPLICANT
        );

        Flux.fromIterable(roles)
                .flatMap(roleName ->
                        roleRepository
                                .findByName(roleName)
                                .switchIfEmpty(roleRepository.save(Role.builder().name(roleName).build()))
                )
                .doOnNext(role -> log.info("Verified or inserted role: {}", role.getName()))
                .subscribe();
    }
}