package identityservice.controller;

import identityservice.dto.request.RoleRequestDto;
import identityservice.dto.response.ApiResponse;
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

    @PostMapping("/create")
    ApiResponse<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleDto) {
        return ApiResponse.<RoleResponseDto>builder()
                .result(roleService.createRole(roleDto))
                .build();
    }

    @PutMapping("/{roleId}")
    ApiResponse<Object> updateRole(@PathVariable String roleId, @RequestBody RoleRequestDto roleDto) {
        return ApiResponse.builder()
                .result(roleService.updateRole(roleId,roleDto))
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<RoleResponseDto>> getAllRoles() {
        return ApiResponse.<List<RoleResponseDto>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable String roleId){
        roleService.deleteRole(roleId);
        return "Deleted a permission with id: " + roleId;
    }
}
