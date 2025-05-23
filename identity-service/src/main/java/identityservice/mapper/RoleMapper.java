package identityservice.mapper;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.RoleResponseDto;
import identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequestDto roleDto);
    RoleResponseDto toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleRequestDto roleRequestDto);
}
