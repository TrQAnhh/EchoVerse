package identityservice.dto.request;

import identityservice.validator.DobConstraint;
import jakarta.validation.constraints.Email;
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

    String firstName;

    String lastName;

    String middleName;

    String phoneNumber;

    @DobConstraint(min = 12, message = "INVALID_DATE_OF_BIRTH")
    LocalDate dob;

    String address;

    String bio;

    String chanelName;
}
