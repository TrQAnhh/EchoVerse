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
    User toUser(UserCreationRequestDto userDto);

    UserResponseDto toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void userUpdate(@MappingTarget User user, UserUpdateRequestDto userDto);
}
