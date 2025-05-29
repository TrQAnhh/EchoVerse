package com.echoverse.profile.controller;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
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

    @GetMapping("/user/{id}")
    ApiResponseDto<UserProfileResponseDto> getProfile(@PathVariable long id) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.getProfile(id)).build();
    }

    @PutMapping("/user/{id}")
    ApiResponseDto<UserProfileResponseDto> editProfile(@PathVariable long id, @RequestBody ProfileCreationRequestDto request) {
        return ApiResponseDto.<UserProfileResponseDto>builder()
                .result(userProfileService.editProfile(id, request)).build();
    }

    @PutMapping("/user/avatar/{id}")
    ApiResponseDto<ImageFileResponseDto> editUserAvatar(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponseDto.<ImageFileResponseDto>builder()
                .code(200)
                .result(userProfileService.editUserAvatar(id,file))
                .build();
    }

    @PutMapping("/user/cover/{id}")
    ApiResponseDto<ImageFileResponseDto> editUserCover(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponseDto.<ImageFileResponseDto>builder()
                .code(200)
                .result(userProfileService.editUserCover(id,file))
                .build();
    }

    @DeleteMapping("/user/{id}")
    ApiResponseDto<Void> deleteProfile(@PathVariable long id) {
        userProfileService.deleteProfile(id);
        return ApiResponseDto.<Void>builder()
                .code(200)
                .message("Delete profile with profile's id: " + id + " successfully")
                .build();
    }

}
