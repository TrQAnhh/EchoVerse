package com.echoverse.video_service.service;

import com.echoverse.video_service.dto.request.VideoCreationRequestDto;
import com.echoverse.video_service.dto.request.VideoUpdateRequestDto;
import com.echoverse.video_service.dto.response.VideoResponseDto;
import com.echoverse.video_service.dto.response.FileResponseDto;
import com.echoverse.video_service.enums.VideoStatus;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VideoService {
    UploadVideoService uploadVideoService;
    VideoRepository videoRepository;
    VideoMapper videoMapper;

    public VideoResponseDto uploadVideo(VideoCreationRequestDto videoCreationRequestDto, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException {
        try {
            FileResponseDto videoFileResponse = uploadVideoService.uploadFile(videoFile);
            FileResponseDto thumbnailFileResponse = uploadVideoService.uploadFile(thumbnailFile);

            long duration = UploadVideoService.getDuration(videoFile);
            Video video = videoMapper.toVideo(videoCreationRequestDto);

            video.setUrl(videoFileResponse.getUrl());
            video.setThumbnailUrl(thumbnailFileResponse.getUrl());
            video.setUploadDate(LocalDateTime.now());
            video.setStatus(VideoStatus.UPLOADED);
            video.setDuration(duration);

            video = videoRepository.save(video);
            return videoMapper.toVideoResponse(video);

        } catch (Exception e) {
            log.error("Error uploading video", e);
            throw new RuntimeException("Upload video failed: " + e.getMessage());
        }
    }

    public VideoResponseDto updateVideo(Long videoId, VideoUpdateRequestDto videoUpdateRequestDto) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        videoMapper.updateVideo(videoUpdateRequestDto, video);

        video = videoRepository.save(video);
        return videoMapper.toVideoResponse(video);
    }

    public List<VideoResponseDto> getAllByUserId(long userId) {
        List<Video> videos = videoRepository.findAllByUserId(userId);
        return videos.stream()
                .map(videoMapper::toVideoResponse)
                .toList();
    }

    public void deleteVideo(long videoId) {
        videoRepository.deleteById(videoId);
    }
}
