package co.com.crediya.r2dbc.repository;

import co.com.crediya.r2dbc.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity,Long> {
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByIdentityNumber(String identityNumber);
    Mono<Boolean> existsByIdentityNumber(String identityNumber);
}
