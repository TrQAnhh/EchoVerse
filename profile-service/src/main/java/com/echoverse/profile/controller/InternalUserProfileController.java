package com.echoverse.profile.controller;

import org.springframework.web.bind.annotation.*;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.response.ApiResponseDto;
import com.echoverse.profile.dto.response.UserProfileResponseDto;
import com.echoverse.profile.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/user")
    UserProfileResponseDto createProfile(@RequestBody ProfileCreationRequestDto request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/internal/user/{userId}")
    ApiResponseDto<UserProfileResponseDto> getProfile(@PathVariable long userId) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.getProfile(userId))
                .build();
    }
}
