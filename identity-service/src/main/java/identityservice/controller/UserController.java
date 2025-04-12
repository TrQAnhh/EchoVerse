package identityservice.controller;

import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.ApiResponse;
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

    @GetMapping("/all")
    public ApiResponse<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ApiResponse.<List<UserResponseDto>>builder()
                .result(users)
                .code(200)
                .message("Successfully fetched all users")
                .build();
    }

    @GetMapping("/get-user/{userId}")
    public UserResponseDto getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/my-info")
    public UserResponseDto getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/update/{id}")
    public UserResponseDto updateUser(@PathVariable String id, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.updateUser(id, userUpdateRequestDto);
    }
}
