package com.echoverse.profile.dto.request;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequestDto {
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
