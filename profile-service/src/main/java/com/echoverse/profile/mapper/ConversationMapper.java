package com.echoverse.profile.mapper;

import com.echoverse.profile.dto.response.ConversationResponseDto;
import com.echoverse.profile.entity.Conversation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    List<ConversationResponseDto> toConversationResponseDto(List<Conversation> conversations);

}
