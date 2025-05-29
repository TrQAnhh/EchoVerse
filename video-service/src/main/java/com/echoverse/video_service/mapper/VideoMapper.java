package com.echoverse.video_service.mapper;
import com.echoverse.video_service.dto.request.VideoCreationRequestDto;
import com.echoverse.video_service.dto.request.VideoUpdateRequestDto;
import com.echoverse.video_service.dto.response.VideoResponseDto;
import com.echoverse.video_service.entity.Video;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel="spring")
public interface VideoMapper {
    VideoResponseDto toVideoResponse(Video video);

    Video toVideo(VideoCreationRequestDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVideoFromDto(VideoUpdateRequestDto dto, @MappingTarget Video entity);
}
