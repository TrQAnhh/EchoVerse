package com.echoverse.video_service.mapper;
import com.echoverse.video_service.dto.request.VideoCreationRequestDto;
import com.echoverse.video_service.dto.request.VideoUpdateRequestDto;
import com.echoverse.video_service.dto.response.VideoResponseDto;
import com.echoverse.video_service.entity.Video;
import org.mapstruct.*;

@Mapper(componentModel="spring")
public interface VideoMapper {
    @Mapping(source = "userId", target = "userId")
    VideoResponseDto toVideoResponse(Video video);

    Video toVideo(VideoCreationRequestDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVideo(VideoUpdateRequestDto dto, @MappingTarget Video video);
}
