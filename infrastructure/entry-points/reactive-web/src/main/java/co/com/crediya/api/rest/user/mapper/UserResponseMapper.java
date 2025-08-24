package co.com.crediya.api.rest.user.mapper;

import co.com.crediya.api.rest.user.dto.UserResponseDto;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseDto toDto(User user);
}
