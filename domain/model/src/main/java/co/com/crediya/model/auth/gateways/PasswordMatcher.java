package co.com.crediya.model.auth.gateways;

import reactor.core.publisher.Mono;

public interface PasswordMatcher {
    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
