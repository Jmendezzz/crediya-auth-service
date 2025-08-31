package co.com.crediya.api.security.jwt;

import co.com.crediya.model.auth.gateways.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenService tokenService;

    public JwtReactiveAuthenticationManager(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication.getCredentials())
                .cast(String.class)
                .flatMap(token -> {
                    if (!tokenService.validateToken(token)) {
                        return Mono.error(new BadCredentialsException("Token inválido"));
                    }
                    return buildAuthentication(token);
                });
    }
    private Mono<Authentication> buildAuthentication(String token) {
        String username = tokenService.extractUsername(token);
        List<String> roles = tokenService.extractRoles(token);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }
}