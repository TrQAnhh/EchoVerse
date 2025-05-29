package identityservice.controller;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.ApiResponseDto;
import identityservice.dto.response.RoleResponseDto;
import identityservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping("")
    ApiResponseDto<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleDto) {
        return ApiResponseDto.<RoleResponseDto>builder()
                .result(roleService.createRole(roleDto))
                .build();
    }

    @PutMapping("/{roleId}")
    ApiResponseDto<Object> updateRole(@PathVariable String roleId, @RequestBody RoleRequestDto roleDto) {
        return ApiResponseDto.builder()
                .result(roleService.updateRole(roleId,roleDto))
                .build();
    }

    @GetMapping("")
    ApiResponseDto<List<RoleResponseDto>> getAllRoles() {
        return ApiResponseDto.<List<RoleResponseDto>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable String roleId){
        roleService.deleteRole(roleId);
        return "Deleted a permission with id: " + roleId;
    }
}
