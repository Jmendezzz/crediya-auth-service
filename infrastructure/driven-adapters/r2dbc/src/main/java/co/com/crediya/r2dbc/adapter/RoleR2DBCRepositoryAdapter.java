package co.com.crediya.r2dbc.adapter;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.mapper.RoleMapper;
import co.com.crediya.r2dbc.repository.RoleReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class RoleR2DBCRepositoryAdapter implements RoleRepository {
    private final RoleReactiveRepository roleReactiveRepository;
    private final RoleMapper roleMapper;

    @Override
    public Mono<Role> findByName(String name) {
        return roleReactiveRepository
                .findByName(name)
                .map(roleMapper::toDomain);
    }

    @Override
    public Flux<Role> findAll() {
        return roleReactiveRepository
                .findAll()
                .map(roleMapper::toDomain);
    }

    @Override
    public Mono<Role> save(Role role) {
        return roleReactiveRepository
                .save(roleMapper.toEntity(role))
                .map(roleMapper::toDomain);
    }
}
