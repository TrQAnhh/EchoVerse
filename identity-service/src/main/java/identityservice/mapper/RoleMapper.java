package identityservice.mapper;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.RoleResponseDto;
import identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "roleName", source = "roleName")
    Role toRole(RoleRequestDto roleDto);
    @Mapping(target = "roleName", source = "roleName")
    RoleResponseDto toRoleResponse(Role role);
}
