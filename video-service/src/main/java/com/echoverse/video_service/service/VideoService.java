package com.echoverse.video_service.service;

import com.echoverse.video_service.dto.request.VideoCreationRequestDto;
import com.echoverse.video_service.dto.request.VideoUpdateRequestDto;
import com.echoverse.video_service.dto.response.VideoResponseDto;
import com.echoverse.video_service.dto.response.FileResponseDto;
import com.echoverse.video_service.mapper.VideoMapper;
import com.echoverse.video_service.repository.VideoRepository;
import com.echoverse.video_service.entity.Video;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VideoService {
    UploadVideoService uploadVideoService;
    VideoRepository videoRepository;
    VideoMapper videoMapper;

    public VideoResponseDto uploadVideo(VideoCreationRequestDto videoCreationRequestDto, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException {
        FileResponseDto videoFileResponse = uploadVideoService.uploadFile(videoFile);
        FileResponseDto thumbnailFileResponse = uploadVideoService.uploadFile(thumbnailFile);

        Video video = videoMapper.toVideo(videoCreationRequestDto);
        video.setUrl(videoFileResponse.getUrl());
        video.setThumbnailUrl(thumbnailFileResponse.getUrl());

        video = videoRepository.save(video);
        return videoMapper.toVideoResponse(video);
    }

    public VideoResponseDto updateVideo(Long videoId, VideoUpdateRequestDto videoUpdateRequestDto) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        videoMapper.updateVideoFromDto(videoUpdateRequestDto, video);

        video = videoRepository.save(video);
        return videoMapper.toVideoResponse(video);
    }
}
