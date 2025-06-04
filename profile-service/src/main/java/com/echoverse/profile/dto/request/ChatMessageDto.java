package com.echoverse.profile.dto.request;

import com.echoverse.profile.enums.MessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDto {
    String content;
    Long senderId;
    Long receiverId;
    MessageType messageType;
}
