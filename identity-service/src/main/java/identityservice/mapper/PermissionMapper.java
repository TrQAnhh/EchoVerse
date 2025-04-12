package identityservice.mapper;

import identityservice.dto.request.PermissionRequestDto;
import identityservice.dto.response.PermissionResponseDto;
import identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequestDto permissionRequestDto);
    PermissionResponseDto toPermissionsResponseDto(Permission permission);
}
