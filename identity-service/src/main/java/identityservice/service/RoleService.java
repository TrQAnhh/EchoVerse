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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponseDto createRole(RoleRequestDto rolesRequestDto) {
        var role = roleMapper.toRole(rolesRequestDto);

        log.info("ROLES: {}", role);

        var permissions = permissionRepository.findAllById(rolesRequestDto.getPermissions());
        log.info("permissions: {}", permissions);
        role.setPermissions(new HashSet<>(permissions));

        log.info("roles: {}", role.getPermissions().stream().toList());

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponseDto> getAllRoles() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }

}
