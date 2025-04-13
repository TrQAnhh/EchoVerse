package identityservice.service;

import identityservice.dto.request.PermissionRequestDto;
import identityservice.dto.response.ApiResponse;
import identityservice.dto.response.PermissionResponseDto;
import identityservice.entity.Permission;
import identityservice.mapper.PermissionMapper;
import identityservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponseDto createPermission(PermissionRequestDto permissionDto) {
        Permission permission = permissionMapper.toPermission(permissionDto);

        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionsResponseDto(permission);
    }

    public List<PermissionResponseDto> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionsResponseDto).toList();
    }

    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }

}
