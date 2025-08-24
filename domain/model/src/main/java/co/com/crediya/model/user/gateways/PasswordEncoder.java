package co.com.crediya.model.user.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncoder {
    Mono<String> encode(String password);
}
