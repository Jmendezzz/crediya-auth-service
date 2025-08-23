package co.com.crediya.r2dbc.repository;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entity.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity,Long> {
    Mono<RoleEntity> findByName(String name);
}
