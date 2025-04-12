package identityservice.service;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.ApiResponse;
import identityservice.dto.response.RoleResponseDto;
import identityservice.mapper.RoleMapper;
import identityservice.repository.PermissionRepository;
import identityservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public ApiResponse<RoleResponseDto> createRole(RoleRequestDto rolesRequestDto) {
        var role = roleMapper.toRole(rolesRequestDto);
        var permissions = permissionRepository.findAllByPermissionNameIn(rolesRequestDto.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return ApiResponse.<RoleResponseDto>builder()
                .code(200)
                .result(roleMapper.toRolesResponseDto(role))
                .build();
    }

    public ApiResponse<List<RoleResponseDto>> getAllRoles() {
        var roles = roleRepository.findAll();
        return ApiResponse.<List<RoleResponseDto>>builder()
                .code(200)
                .result(roles.stream().map(roleMapper::toRolesResponseDto).toList())
                .build();
    }

    public void deleteRole(String roleName) {
        roleRepository.deleteByRoleName(roleName);
    }
}
