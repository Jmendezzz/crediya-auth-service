package co.com.crediya.r2dbc.adapter;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.mapper.RoleMapper;
import co.com.crediya.r2dbc.mapper.UserMapper;
import co.com.crediya.r2dbc.repository.RoleReactiveRepository;
import co.com.crediya.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserR2DBCRepositoryAdapter implements UserRepository {
    private final UserReactiveRepository userReactiveRepository;
    private final RoleReactiveRepository roleReactiveRepository;
    private final TransactionalOperator transactionalOperator;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public Mono<User> save(User user) {
        return transactionalOperator.transactional(
                userReactiveRepository
                        .save(userMapper.toEntity(user))
                        .flatMap(this::enrichWithRole)
        );
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userReactiveRepository.findByEmail(email)
                .flatMap(this::enrichWithRole);
    }

    @Override
    public Mono<User> findByIdentityNumber(String identityNumber) {
        return userReactiveRepository.findByIdentityNumber(identityNumber)
                .flatMap(this::enrichWithRole);
    }


    @Override
    public Mono<Boolean> existsByIdentityNumber(String identityNumber) {
        return userReactiveRepository.existsByIdentityNumber(identityNumber);
    }

    private Mono<User> enrichWithRole(UserEntity userEntity) {
        return roleReactiveRepository.findById(userEntity.getRoleId())
                .map(roleEntity -> userMapper.toDomain(
                        userEntity,
                        roleMapper.toDomain(roleEntity)
                ));
    }
}

