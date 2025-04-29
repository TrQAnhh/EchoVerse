package com.echoverse.profile.controller;

import com.echoverse.profile.dto.request.ProfileCreationRequest;
import com.echoverse.profile.dto.response.ApiResponse;
import com.echoverse.profile.dto.response.UserProfileResponse;
import com.echoverse.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/user")
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createProfile(request)).build();
    }

    @GetMapping
    ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles()).build();
    }

    @GetMapping("/user/{id}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable long id) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getProfile(id)).build();
    }

    @PutMapping("/user/{id}")
    ApiResponse<UserProfileResponse> editProfile(@PathVariable long id, @RequestBody ProfileCreationRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.editProfile(id, request)).build();
    }

    @DeleteMapping("/user/{id}")
    ApiResponse<Void> deleteProfile(@PathVariable long id) {
        userProfileService.deleteProfile(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete profile with profile's id: " + id + " successfully")
                .build();
    }

}
