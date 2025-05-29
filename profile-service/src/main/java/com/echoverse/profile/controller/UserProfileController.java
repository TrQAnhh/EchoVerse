package com.echoverse.profile.controller;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.request.ProfileUpdateRequestDto;
import com.echoverse.profile.dto.response.ApiResponseDto;
import com.echoverse.profile.dto.response.ImageFileResponseDto;
import com.echoverse.profile.dto.response.UserProfileResponseDto;
import com.echoverse.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/user")
    ApiResponseDto<UserProfileResponseDto> createProfile(@RequestBody ProfileCreationRequestDto request) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.createProfile(request)).build();
    }

    @GetMapping
    ApiResponseDto<List<UserProfileResponseDto>> getAllProfiles() {
        return ApiResponseDto.<List<UserProfileResponseDto>>builder()
                .result(userProfileService.getAllProfiles()).build();
    }

    @GetMapping("/user/{userId}")
    ApiResponseDto<UserProfileResponseDto> getProfile(@PathVariable long userId) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.getProfile(userId)).build();
    }

    @PutMapping("/user/{userId}")
    ApiResponseDto<UserProfileResponseDto> editProfile(@PathVariable long userId, @RequestBody ProfileUpdateRequestDto request) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.editProfile(userId, request)).build();
    }

    @PutMapping("/user/avatar/{userId}")
    ApiResponseDto<ImageFileResponseDto> editUserAvatar(@PathVariable long userId, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponseDto.<ImageFileResponseDto>builder()
                .code(200)
                .result(userProfileService.editUserAvatar(userId,file))
                .build();
    }

    @PutMapping("/user/cover/{userId}")
    ApiResponseDto<ImageFileResponseDto> editUserCover(@PathVariable long userId, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponseDto.<ImageFileResponseDto>builder()
                .code(200)
                .result(userProfileService.editUserCover(userId,file))
                .build();
    }

    @DeleteMapping("/user/{userId}")
    ApiResponseDto<Void> deleteProfile(@PathVariable long userId) {
        userProfileService.deleteProfile(userId);
        return ApiResponseDto.<Void>builder()
                .code(200)
                .message("Delete profile with user's id: " + userId + " successfully")
                .build();
    }

}
