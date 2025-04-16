package identityservice.mapper;

import identityservice.dto.request.PermissionRequestDto;
import identityservice.dto.response.PermissionResponseDto;
import identityservice.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "name",source = "name")
    Permission toPermission(PermissionRequestDto permissionRequestDto);
    PermissionResponseDto toPermissionsResponseDto(Permission permission);
    void updatePermission(@MappingTarget Permission permission, PermissionRequestDto permissionRequestDto);
}
