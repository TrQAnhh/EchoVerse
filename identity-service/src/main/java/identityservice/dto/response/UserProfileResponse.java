package identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    long id;
    String firstName;
    String middleName;
    String lastName;
    String phoneNumber;
    String chanelName;
    Set<String> subscribers;
    String email;
    LocalDate dob;
    String address;
    String bio;
    String coverImage;
    String avatar;
    Date createdt;
    Date updatedt;
}
