package identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {
    String name;
    String description;
    Set<PermissionResponseDto> permissions;
}
