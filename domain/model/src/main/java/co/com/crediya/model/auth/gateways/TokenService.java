package co.com.crediya.model.auth.gateways;

import co.com.crediya.model.user.User;

import java.util.List;

public interface TokenService {
    String generateToken(User user);
    boolean validateToken(String token);
    String extractUsername(String token);
    List<String> extractRoles(String token);
}
