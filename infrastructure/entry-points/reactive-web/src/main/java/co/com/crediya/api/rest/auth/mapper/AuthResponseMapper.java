package co.com.crediya.api.rest.auth.mapper;

import co.com.crediya.api.rest.auth.dto.LoginResponseDto;
import co.com.crediya.model.auth.result.LoginResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthResponseMapper {
    LoginResponseDto toDto(LoginResult loginResult);
}
