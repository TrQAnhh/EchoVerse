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
    public ApiResponse<RoleResponseDto> createRole(@RequestBody RoleRequestDto rolesRequestDto) {
        return roleService.createRole(rolesRequestDto);
    }

    @GetMapping("/all")
    public ApiResponse<List<RoleResponseDto>> getAllRoles() {
        return roleService.getAllRoles();
    }
}
