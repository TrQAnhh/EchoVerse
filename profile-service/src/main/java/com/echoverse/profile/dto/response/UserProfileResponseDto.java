package com.echoverse.profile.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponseDto {
    long id;
    long userId;
    String email;
    String firstName;
    String lastName;
    String middleName;
    String phoneNumber;
    LocalDate dob;
    String address;
    String bio;
    String chanelName;

    String avatar;
    String coverImage;
    Set<String> subscribers;
}
