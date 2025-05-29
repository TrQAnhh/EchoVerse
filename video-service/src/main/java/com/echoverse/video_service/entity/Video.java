package com.echoverse.video_service.entity;

import com.echoverse.video_service.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Column(length = 5000)
    String description;

    String url;

    String thumbnailUrl;

    Long duration;

    LocalDateTime uploadDate;

    Long userId;

    @Enumerated(EnumType.STRING)
    VideoStatus status;

    Long views;

    Boolean isLive;

    LocalDateTime liveStartTime;

    LocalDateTime liveEndTime;
}
