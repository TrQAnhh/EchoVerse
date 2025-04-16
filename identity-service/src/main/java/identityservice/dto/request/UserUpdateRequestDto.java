package identityservice.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateRequestDto {
    String email;
    String password;
    LocalDate date_of_birth;
    String middleName;
    String firstName;
    String lastName;
    String phoneNumber;
    String address;
    String bio;
    String chanelName;
    List<Long> following;
    List<Long> followers;
    List<String> roles;
}
