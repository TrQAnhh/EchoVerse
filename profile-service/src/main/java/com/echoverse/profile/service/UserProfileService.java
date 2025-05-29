package com.echoverse.profile.service;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.request.ProfileUpdateRequestDto;
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
import org.springframework.security.access.prepost.PostAuthorize;
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

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    public UserProfileResponseDto getProfile(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    public UserProfileResponseDto editProfile(long userId, ProfileUpdateRequestDto request) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfileMapper.updateUserProfile(userProfile, request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    public ImageFileResponseDto editUserAvatar(long userId, MultipartFile file) throws IOException {
        return updateProfileImage(userId, file, UserProfile::setAvatar);
    }

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    public ImageFileResponseDto editUserCover(long userId, MultipartFile file) throws IOException {
        return updateProfileImage(userId, file, UserProfile::setCoverImage);
    }

    private ImageFileResponseDto updateProfileImage(
            long userId,
            MultipartFile file,
            BiConsumer<UserProfile, String> setter
    ) throws IOException {

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        ImageFileResponseDto img = uploadImageService.uploadImage(file);
        setter.accept(profile, img.getUrl());
        userProfileRepository.save(profile);
        return ImageFileResponseDto.builder()
                .userId(userId)
                .originalFileName(img.getOriginalFileName())
                .url(img.getUrl())
                .build();
    }

    public void deleteProfile(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfile.setDeleted(true);
        userProfileRepository.save(userProfile);
    }
}

