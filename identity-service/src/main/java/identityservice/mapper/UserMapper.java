package identityservice.mapper;

import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.UserResponseDto;
import identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target="roles", ignore = true)
    User toUser(UserCreationRequestDto userCreationRequestDto);


    UserResponseDto toUserResponseDto(User user);

    @Mapping(target="roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequestDto userUpdateRequestDto);
}
