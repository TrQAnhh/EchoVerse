package com.echoverse.profile.mapper;

import com.echoverse.profile.dto.response.UserContactResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.echoverse.profile.dto.request.ProfileCreationRequestDto;
import com.echoverse.profile.dto.request.ProfileUpdateRequestDto;
import com.echoverse.profile.dto.response.UserProfileResponseDto;
import com.echoverse.profile.entity.UserProfile;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserProfileMapper {
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "chanelName", target = "chanelName")
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "coverImage", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "createdt", ignore = true)
    @Mapping(target = "updatedt", ignore = true)
    UserProfile toUserProfile(ProfileCreationRequestDto request);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "coverImage", target = "coverImage")
    UserProfileResponseDto toUserProfileResponse(UserProfile userProfile);

    UserContactResponseDto toUserContactResponse(UserProfile userProfile);

    void updateUserProfile(@MappingTarget UserProfile profile, ProfileUpdateRequestDto request);
}