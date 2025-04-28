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

    @GetMapping()
    ApiResponse<List<PermissionResponseDto>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponseDto>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }

    @PostMapping()
    public ApiResponse<PermissionResponseDto> createPermission(@RequestBody PermissionRequestDto permissionRequestDto) {
        return ApiResponse.<PermissionResponseDto>builder()
                .result(permissionService.createPermission(permissionRequestDto))
                .build();    }

    @PutMapping("/{permissionId}")
    public ApiResponse<PermissionResponseDto> updatePermission(@RequestBody PermissionRequestDto permissionRequestDto, @PathVariable String permissionId) {
        return ApiResponse.<PermissionResponseDto>builder()
                .result(permissionService.updatePermission(permissionId,permissionRequestDto))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    public String deletePermission(@PathVariable String permissionId) {
        permissionService.deletePermission(permissionId);
        return "Deleted a permission with id: " + permissionId;
    }
}
