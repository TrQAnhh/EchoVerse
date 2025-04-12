package identityservice.controller;

import identityservice.dto.request.PermissionRequestDto;
import identityservice.dto.response.ApiResponse;
import identityservice.dto.response.PermissionResponseDto;
import identityservice.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @GetMapping("/all")
    public ApiResponse<List<PermissionResponseDto>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @PostMapping("/create")
    public ApiResponse<PermissionResponseDto> createPermission(@RequestBody PermissionRequestDto permissionRequestDto) {
        return permissionService.createPermission(permissionRequestDto);
    }
}
