package com.echoverse.profile.service;

import com.echoverse.profile.dto.request.ProfileCreationRequest;
import com.echoverse.profile.dto.response.UserProfileResponse;
import com.echoverse.profile.entity.UserProfile;
import com.echoverse.profile.mapper.UserProfileMapper;
import com.echoverse.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        System.out.println("Received request: " + request);

        UserProfile userProfile = userProfileMapper.toUserProfile(request);

        System.out.println("Mapped UserProfile: " + userProfile);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new RuntimeException("Profile not found"));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse editProfile(long profileId, ProfileCreationRequest request) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new RuntimeException("Profile not found"));

        userProfileMapper.profileUpdate(userProfile, request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse deleteProfile(long profileId) {
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(
                () -> new RuntimeException("Profile not found"));

        userProfileRepository.deleteById(profileId);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}

