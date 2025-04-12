package identityservice.dto.request;

import identityservice.validator.DobConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequestDto {

    @NotEmpty(message = "NOT_NULL_CONSTRAINT")
    String username;

    @NotEmpty(message = "NOT_NULL_CONSTRAINT")
    String password;

    String email;
    String phoneNumber;

    @NotNull(message = "NOT_NULL_CONSTRAINT")
    @DobConstraint(min = 12, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dateOfBirth;

    List<String> roles;
}
