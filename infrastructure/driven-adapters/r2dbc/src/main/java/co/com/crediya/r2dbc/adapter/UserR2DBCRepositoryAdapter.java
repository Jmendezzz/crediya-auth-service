package co.com.crediya.r2dbc.adapter;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.constant.UserRepositoryLog;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.mapper.RoleMapper;
import co.com.crediya.r2dbc.mapper.UserMapper;
import co.com.crediya.r2dbc.repository.RoleReactiveRepository;
import co.com.crediya.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
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
        log.info(UserRepositoryLog.SAVE_START.getMessage(), user.getIdentityNumber());

        return transactionalOperator.transactional(
                userReactiveRepository.save(userMapper.toEntity(user))
                        .flatMap(this::enrichWithRole)
                        .doOnSuccess(saved -> log.info(UserRepositoryLog.SAVE_SUCCESS.getMessage(), saved.getId()))
                        .doOnError(error -> log.error(UserRepositoryLog.SAVE_ERROR.getMessage(), user.getIdentityNumber(), error.getMessage(), error))
        );
    }

    @Override
    public Mono<User> findById(Long id) {
        return userReactiveRepository.findById(id)
                .flatMap(this::enrichWithRole);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        log.debug(UserRepositoryLog.FIND_BY_EMAIL.getMessage(), email);

        return userReactiveRepository.findByEmail(email)
                .flatMap(this::enrichWithRole)
                .doOnSuccess(user -> {
                    if (user != null) {
                        log.info(UserRepositoryLog.FIND_BY_EMAIL_SUCCESS.getMessage(), email);
                    } else {
                        log.warn(UserRepositoryLog.FIND_BY_EMAIL_NOT_FOUND.getMessage(), email);
                    }
                })
                .doOnError(error -> log.error(UserRepositoryLog.FIND_BY_EMAIL_ERROR.getMessage(), email, error.getMessage(), error));
    }

    @Override
    public Mono<User> findByIdentityNumber(String identityNumber) {
        log.debug(UserRepositoryLog.FIND_BY_IDENTITY.getMessage(), identityNumber);

        return userReactiveRepository.findByIdentityNumber(identityNumber)
                .flatMap(this::enrichWithRole)
                .doOnSuccess(user -> {
                    if (user != null) {
                        log.info(UserRepositoryLog.FIND_BY_IDENTITY_SUCCESS.getMessage(), identityNumber);
                    } else {
                        log.warn(UserRepositoryLog.FIND_BY_IDENTITY_NOT_FOUND.getMessage(), identityNumber);
                    }
                })
                .doOnError(error -> log.error(UserRepositoryLog.FIND_BY_IDENTITY_ERROR.getMessage(), identityNumber, error.getMessage(), error));
    }

    @Override
    public Mono<Boolean> existsByIdentityNumber(String identityNumber) {
        log.debug(UserRepositoryLog.EXISTS_BY_IDENTITY.getMessage(), identityNumber);

        return userReactiveRepository.existsByIdentityNumber(identityNumber)
                .doOnSuccess(exists -> log.info(UserRepositoryLog.EXISTS_BY_IDENTITY_RESULT.getMessage(), identityNumber, exists));
    }

    private Mono<User> enrichWithRole(UserEntity userEntity) {
        log.debug(UserRepositoryLog.ENRICH_START.getMessage(), userEntity.getId(), userEntity.getRoleId());

        return roleReactiveRepository.findById(userEntity.getRoleId())
                .map(roleEntity -> {
                    User user = userMapper.toDomain(
                            userEntity,
                            roleMapper.toDomain(roleEntity)
                    );
                    log.debug(UserRepositoryLog.ENRICH_SUCCESS.getMessage(), userEntity.getId(), roleEntity.getName());
                    return user;
                })
                .doOnError(error -> log.error(UserRepositoryLog.ENRICH_ERROR.getMessage(), userEntity.getId(), error.getMessage(), error));
    }
}
