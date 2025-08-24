package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.id",target = "roleId")
    UserEntity toEntity(User user);

    @Mapping(source = "userEntity.id", target = "id")
    @Mapping(source = "role", target = "role")
    User toDomain(UserEntity userEntity, Role role);
}
