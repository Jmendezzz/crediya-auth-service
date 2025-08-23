package co.com.crediya.r2dbc.mapper;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toDomain(RoleEntity role);

    RoleEntity toEntity(Role role);
}
