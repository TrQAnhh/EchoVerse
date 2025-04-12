package identityservice.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateRequestDto {
    @Id
    String id;
    String username;
    String email;
    String password;
    LocalDate date_of_birth;
    List<String> roles;
}
