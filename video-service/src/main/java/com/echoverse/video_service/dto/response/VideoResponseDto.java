package com.echoverse.video_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoResponseDto {
    Long id;
    String title;
    String description;
    String url;
    String thumbnailUrl;
    Long duration;
    LocalDateTime uploadDate;
    Long createdBy;
    String status;
    Long views;
    Boolean isLive;
    LocalDateTime liveStartTime;
    LocalDateTime liveEndTime;
}
