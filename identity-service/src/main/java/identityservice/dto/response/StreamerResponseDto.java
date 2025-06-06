package identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StreamerResponseDto {
    long id;
    String name;
    String email;
    String avatarUrl;
}
