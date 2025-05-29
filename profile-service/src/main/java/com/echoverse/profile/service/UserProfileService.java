package com.echoverse.profile.service;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.response.ImageFileResponseDto;
import com.echoverse.profile.dto.response.UserProfileResponseDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    UploadImageService uploadImageService;

    public UserProfileResponseDto createProfile(ProfileCreationRequestDto request) {
        System.out.println("Received request: " + request);

        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        System.out.println("Mapped UserProfile: " + userProfile);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponseDto> getAllProfiles() {
        List<UserProfileResponseDto> userProfiles = userProfileRepository.findAll().stream().map(userProfileMapper::toUserProfileResponse).toList();
        return userProfiles;
    }

    public UserProfileResponseDto getProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponseDto editProfile(long profileId, ProfileCreationRequestDto request) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfileMapper.profileUpdate(userProfile, request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public ImageFileResponseDto editUserAvatar(long profileId, MultipartFile file) throws IOException {
        return updateProfileImage(profileId, file, UserProfile::setAvatar);
    }

    public ImageFileResponseDto editUserCover(long profileId, MultipartFile file) throws IOException {
        return updateProfileImage(profileId, file, UserProfile::setCoverImage);
    }

    private ImageFileResponseDto updateProfileImage(
            long profileId,
            MultipartFile file,
            BiConsumer<UserProfile, String> setter
    ) throws IOException {
        String currentUserId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        if (!String.valueOf(profile.getUserId()).equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        ImageFileResponseDto img = uploadImageService.uploadImage(file);
        setter.accept(profile, img.getUrl());
        userProfileRepository.save(profile);
        return img;
    }


    public void deleteProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfile.setDeleted(true);
        userProfileRepository.save(userProfile);
    }
}

