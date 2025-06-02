package com.echoverse.profile.dto.response;

import com.echoverse.profile.entity.Message;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponseDto {
    Long conversationId;
    Long userOneId;
    Long userTwoId;
    String lastMessage;
    LocalDateTime lastSentAt;
}
