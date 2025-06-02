package com.echoverse.profile.entity;

import com.echoverse.profile.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @JsonIgnore
    Conversation conversation;

    Long senderId;
    Long receiverId;

    @Column(columnDefinition = "TEXT")
    String message;

    LocalDateTime sentAt;

    boolean isRead;

    MessageType messageType;
}
