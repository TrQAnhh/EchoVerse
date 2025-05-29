package com.echoverse.profile.controller;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.response.ApiResponseDto;
import com.echoverse.profile.dto.response.UserProfileResponseDto;
import com.echoverse.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/user")
    UserProfileResponseDto createProfile(@RequestBody ProfileCreationRequestDto request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/internal/user/{id}")
    ApiResponseDto<UserProfileResponseDto> getProfile(@PathVariable long id) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.getProfile(id)).build();
    }

}
