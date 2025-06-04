package com.echoverse.profile.controller;

import com.echoverse.profile.dto.response.ApiResponseDto;
import com.echoverse.profile.dto.response.ConversationResponseDto;
import com.echoverse.profile.dto.response.MessageResponseDto;
import com.echoverse.profile.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/conversations")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ConversationController {
    ConversationService conversationService;

    @GetMapping("/{userId}")
    public ApiResponseDto<List<ConversationResponseDto>> getAllByUserId(@PathVariable Long userId) {
        return ApiResponseDto.<List<ConversationResponseDto>>builder()
                .code(200)
                .result(conversationService.getAllByUserId(userId))
                .build();
    }

    @GetMapping("/messages/{conversationId}")
    public ApiResponseDto<List<MessageResponseDto>> getAllMessages(@PathVariable long conversationId) throws Exception {
        return ApiResponseDto.<List<MessageResponseDto>>builder()
                .code(200)
                .result(conversationService.getAllMessages(conversationId))
                .build();
    }

    @DeleteMapping("/{conversationId}")
    public ApiResponseDto<Void> deleteConversation(@PathVariable long conversationId) {
        conversationService.deleteConversation(conversationId);
        return ApiResponseDto.<Void>builder()
                .code(200)
                .message("Conversation deleted successfully")
                .build();
    }
}
