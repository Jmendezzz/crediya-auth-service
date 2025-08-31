package co.com.crediya.model.auth.command;

public record LoginCommand(
        String email,
        String password
) {
}
