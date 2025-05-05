package com.echoverse.profile.service;

import com.echoverse.profile.dto.request.ProfileCreationRequest;
import com.echoverse.profile.dto.response.ImageFileResponse;
import com.echoverse.profile.dto.response.UserProfileResponse;
import com.echoverse.profile.entity.UserProfile;
import com.echoverse.profile.exception.AppException;
import com.echoverse.profile.exception.ErrorCode;
import com.echoverse.profile.mapper.UserProfileMapper;
import com.echoverse.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    UploadImageService uploadImageService;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        System.out.println("Received request: " + request);

        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        System.out.println("Mapped UserProfile: " + userProfile);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfileResponse> userProfiles = userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileResponse).toList();
        return userProfiles;
    }

    public UserProfileResponse getProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse editProfile(long profileId, ProfileCreationRequest request) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfileMapper.profileUpdate(userProfile, request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public ImageFileResponse editUserAvatar(long profileId, MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        if (!String.valueOf(userProfile.getUserId()).equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var fileUpload = uploadImageService.uploadImage(file);

        userProfile.setAvatar(fileUpload.getUrl());
        userProfile = userProfileRepository.save(userProfile);

        return ImageFileResponse.builder()
                .url(fileUpload.getUrl())
                .originalFileName(fileUpload.getOriginalFileName())
                .build();
    }

    public void deleteProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfile.setDeleted(true);
        userProfileRepository.save(userProfile);
    }
}

