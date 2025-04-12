package identityservice.mapper;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.PermissionResponseDto;
import identityservice.dto.response.RoleResponseDto;
import identityservice.entity.Permission;
import identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequestDto rolesRequestDto);

    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionsToRolesResponseDto")
    RoleResponseDto toRolesResponseDto(Role role);

    @Named("mapPermissionsToRolesResponseDto")
    default Set<PermissionResponseDto> mapPermissionsToRolesResponseDto(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(permission -> new PermissionResponseDto(permission.getPermissionName(), permission.getDescription()))
                .collect(Collectors.toSet());
    }
}
