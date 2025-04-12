package identityservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequestDto {
    @NotEmpty(message = "NOT_NULL_CONSTRAINT")
    String username;
    @NotEmpty(message = "NOT_NULL_CONSTRAINT")
    String password;
}
