package identityservice.mapper;

import identityservice.dto.request.ProfileCreationRequestDto;
import identityservice.dto.request.UserCreationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "middleName", target = "middleName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "chanelName", target = "chanelName")
    ProfileCreationRequestDto toProfileCreationRequest(UserCreationRequestDto userDto);
}
