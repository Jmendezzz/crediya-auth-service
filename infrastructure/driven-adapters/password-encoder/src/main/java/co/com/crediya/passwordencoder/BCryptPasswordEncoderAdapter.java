package co.com.crediya.passwordencoder;

import co.com.crediya.model.auth.gateways.PasswordMatcher;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoder, PasswordMatcher {

    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public Mono<String> encode(String password) {
        return Mono.fromCallable(() -> delegate.encode(password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
        return Mono.fromCallable(() -> delegate.matches(rawPassword, encodedPassword))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
