package com.echoverse.profile.controller;

import com.echoverse.profile.dto.request.ChatMessageDto;
import com.echoverse.profile.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ChattingController {
    SimpMessagingTemplate messagingTemplate;
    ConversationService conversationService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) throws Exception {
        conversationService.saveMessage(chatMessageDto);
        Long senderId = chatMessageDto.getSenderId();
        Long receiverId = chatMessageDto.getReceiverId();

        String topic = "/topic/conversation." + Math.min(senderId, receiverId) + "_" + Math.max(senderId, receiverId);
        messagingTemplate.convertAndSend(topic, chatMessageDto);
    }
}
