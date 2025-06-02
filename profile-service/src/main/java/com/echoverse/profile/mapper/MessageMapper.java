package com.echoverse.profile.mapper;

import com.echoverse.profile.dto.response.MessageResponseDto;
import com.echoverse.profile.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    List<MessageResponseDto> toMessageResponseDto(List<Message> messageList);
}
