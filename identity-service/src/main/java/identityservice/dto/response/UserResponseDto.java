package identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    long id;
    String username;
    UserProfileResponseDto profile;
    Set<RoleResponseDto> roles;
    StreamerResponseDto streamer;
}
