package identityservice.controller;

import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.ApiResponseDto;
import identityservice.dto.response.UserResponseDto;
import identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponseDto<List<UserResponseDto>> getUsers() {
        return ApiResponseDto.<List<UserResponseDto>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponseDto<UserResponseDto> getUser(@PathVariable Long userId) {
        return ApiResponseDto.<UserResponseDto>builder().
                result(userService.getUser(userId))
        .build();
    }

    @GetMapping("/personal")
    ApiResponseDto<UserResponseDto> userInformation() {
        return ApiResponseDto.<UserResponseDto>builder().result(userService.getMyInfo()).build();
    }


    @PutMapping("/{userId}")
    ApiResponseDto<UserResponseDto> updateUser(@RequestBody UserUpdateRequestDto userDto, @PathVariable Long userId) {
        return ApiResponseDto.<UserResponseDto>builder().result(userService.updateUser(userId, userDto)).build();
    }

    @DeleteMapping("/{userId}")
    ApiResponseDto<Void> deleteUser(@PathVariable Long userId) {
        return ApiResponseDto.<Void>builder()
                .code(200)
                .message("Delete user with user's id: " + userId + " successfully")
                .build();
    }
}
