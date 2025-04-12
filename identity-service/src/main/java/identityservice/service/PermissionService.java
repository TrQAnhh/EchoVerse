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

    public ApiResponse<PermissionResponseDto> createPermission(PermissionRequestDto permissionRequestDto) {
        Permission permission = permissionMapper.toPermission(permissionRequestDto);
        Permission result = permissionRepository.save(permission);
        return ApiResponse.<PermissionResponseDto>builder()
                .code(200)
                .result(permissionMapper.toPermissionsResponseDto(result))
                .build();
    }

    public ApiResponse<List<PermissionResponseDto>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponseDto> responseDtoList = permissions.stream()
                .map(permissionMapper::toPermissionsResponseDto)
                .toList();
        return ApiResponse.<List<PermissionResponseDto>>builder()
                .code(200)
                .result(responseDtoList)
                .build();
    }

    public ApiResponse<Void> deletePermission(String permissionName) {
        permissionRepository.deleteByPermissionName(permissionName);
        return ApiResponse.<Void>builder()
                .code(200)
                .build();
    }
}
