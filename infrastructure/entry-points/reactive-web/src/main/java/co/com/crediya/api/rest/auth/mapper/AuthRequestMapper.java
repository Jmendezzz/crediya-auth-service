package co.com.crediya.api.rest.auth.mapper;

import co.com.crediya.api.rest.auth.dto.LoginRequestDto;
import co.com.crediya.model.auth.command.LoginCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthRequestMapper {
    LoginCommand toDomain(LoginRequestDto loginRequestDto);
}
