package identityservice.controller;

import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.ApiResponse;
import identityservice.dto.response.UserResponseDto;
import identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class UserController {

    UserService userService;

    @GetMapping
    ApiResponse<List<UserResponseDto>> getUsers(@RequestParam(required = false) Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponseDto>>builder()
                .result(userService.getUsers(id))
                .build();
    }

    @GetMapping("/personal")
    UserResponseDto userInformation() {
        return userService.getMyInfo();
    }


    @PutMapping("/update-info/{userId}")
    UserResponseDto updateUser(@RequestBody UserUpdateRequestDto userDto, @PathVariable Long userId) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "Deleted user with id: " + userId;
    }
}
