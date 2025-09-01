package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findByEmail(String email);
    Mono<User> findByIdentityNumber(String identityNumber);
    Mono<User> save(User user);
    Mono<User> findById(Long id);
    Mono<Boolean> existsByIdentityNumber(String identityNumber);
}