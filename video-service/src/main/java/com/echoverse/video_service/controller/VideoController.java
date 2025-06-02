package com.echoverse.video_service.controller;

import com.echoverse.video_service.dto.request.VideoCreationRequestDto;
import com.echoverse.video_service.dto.request.VideoUpdateRequestDto;
import com.echoverse.video_service.dto.response.ApiResponseDto;
import com.echoverse.video_service.dto.response.VideoResponseDto;
import com.echoverse.video_service.service.VideoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VideoController {
    VideoService videoService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResponseDto<VideoResponseDto> uploadVideo(
            @RequestPart("videoFile") MultipartFile videoFile,
            @RequestPart("thumbnailFile") MultipartFile thumbnailFile,
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("userId") String userId,
            @RequestPart("isLive") String isLive) throws IOException {

        VideoCreationRequestDto videoCreationRequestDto = VideoCreationRequestDto.builder()
                .title(title)
                .description(description)
                .userId(parseLong(userId))
                .isLive(parseBoolean(isLive))
                .build();

        return ApiResponseDto.<VideoResponseDto>builder()
                .code(200)
                .result(videoService.uploadVideo(videoCreationRequestDto, videoFile, thumbnailFile))
                .build();
    }

    @PutMapping("/{videoId}")
    public ApiResponseDto<VideoResponseDto> updateVideo(
            @PathVariable("videoId") long videoId,
            @RequestBody() VideoUpdateRequestDto videoUpdateRequestDto
            ) {
        return ApiResponseDto.<VideoResponseDto>builder()
                .code(200)
                .result(videoService.updateVideo(videoId, videoUpdateRequestDto))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponseDto<List<VideoResponseDto>> getAllVideos(@PathVariable("userId") long userId) {
        return ApiResponseDto.<List<VideoResponseDto>>builder()
                .code(200)
                .result(videoService.getAllByUserId(userId))
                .build();
    }

    @DeleteMapping("/{videoId}")
    public ApiResponseDto<Void> deleteVideo(@PathVariable("videoId") long videoId) {
        videoService.deleteVideo(videoId);
        return ApiResponseDto.<Void>builder()
                .code(200)
                .message("Delete video successfully")
                .build();
    }
}

