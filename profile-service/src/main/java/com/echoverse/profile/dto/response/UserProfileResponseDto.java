package com.echoverse.profile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponseDto {
    long id;
    String email;

    String firstName;

    String lastName;

    String middleName;

    String phoneNumber;
    LocalDate dob;

    String address;

    String bio;

    String chanelName;
}
