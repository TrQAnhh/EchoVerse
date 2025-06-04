package com.echoverse.profile.dto.response;

import com.echoverse.profile.enums.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponseDto {
    Long messageId;
    Long senderId;
    Long receiverId;
    String message;
    LocalDateTime sentAt;
    boolean isRead;
    MessageType messageType;
}
