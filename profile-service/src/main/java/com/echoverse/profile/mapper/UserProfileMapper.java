package com.echoverse.profile.mapper;

import com.echoverse.profile.dto.request.ProfileCreationRequest;
import com.echoverse.profile.dto.response.UserProfileResponse;
import com.echoverse.profile.entity.UserProfile;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
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
    UserProfile toUserProfile(ProfileCreationRequest request);


    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    void profileUpdate(@MappingTarget UserProfile profile, ProfileCreationRequest request);
}
