package co.com.crediya.usecase.user;

import co.com.crediya.model.role.constants.RoleConstant;
import co.com.crediya.model.role.exceptions.RoleNotFoundException;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.exceptions.UserEmailAlreadyExistsException;
import co.com.crediya.model.user.exceptions.UserIdentityNumberAlreadyExistsException;
import co.com.crediya.model.user.gateways.PasswordEncoder;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> createApplicant(User user){
        return validateUniqueness(user)
                .then(roleRepository.findByName(RoleConstant.APPLICANT))
                .switchIfEmpty(Mono.error(new RoleNotFoundException(RoleConstant.APPLICANT)))
                .zipWhen(role -> passwordEncoder.encode(user.getPassword()),
                        (role, hash) -> user.toBuilder().password(hash).role(role).build()
                )
                .flatMap(userRepository::save);
    }

    private Mono<Void> validateUniqueness(User user){
        return Mono.zip(
                userRepository.findByEmail(user.getEmail()).hasElement(),
                userRepository.findByIdentityNumber(user.getIdentityNumber()).hasElement()
        ).flatMap(tuple -> {
            if (tuple.getT1()) return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
            if (tuple.getT2()) return Mono.error(new UserIdentityNumberAlreadyExistsException(user.getIdentityNumber()));
            return Mono.empty();
        });
    }

    public Mono<Boolean> existsByIdentityNumber(String identityNumber) {
        return userRepository.findByIdentityNumber(identityNumber)
                .hasElement();
    }
}
