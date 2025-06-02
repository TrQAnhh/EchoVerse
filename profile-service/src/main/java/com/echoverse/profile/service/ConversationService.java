package com.echoverse.profile.service;


import com.echoverse.profile.dto.request.ChatMessageDto;
import com.echoverse.profile.dto.response.ConversationResponseDto;
import com.echoverse.profile.dto.response.MessageResponseDto;
import com.echoverse.profile.entity.Conversation;
import com.echoverse.profile.entity.Message;
import com.echoverse.profile.mapper.ConversationMapper;
import com.echoverse.profile.mapper.MessageMapper;
import com.echoverse.profile.repository.ConversationRepository;
import com.echoverse.profile.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConversationService {
    ConversationRepository conversationRepository;
    MessageRepository messageRepository;
    ConversationMapper conversationMapper;
    MessageMapper messageMapper;

    public List<ConversationResponseDto> getAllByUserId(long userId) {
        var conversations = conversationRepository.findAllByUserId(userId);
        return conversationMapper.toConversationResponseDto(conversations);
    }

    public List<MessageResponseDto> getAllMessages(long conversationId) {
        var conversation = conversationRepository.findById(conversationId);
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messageMapper.toMessageResponseDto(messages);
    }

    public void deleteConversation(long conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    @Transactional
    public void saveMessage(ChatMessageDto dto) {
        Long sender = dto.getSenderId();
        Long receiver = dto.getReceiverId();

        var optionalConversation = conversationRepository.findByUserIds(sender, receiver);
        Conversation conversation;
        if (optionalConversation.isPresent()) {
            conversation = optionalConversation.get();
        } else {
            conversation = Conversation.builder()
                    .userOneId(Math.min(sender, receiver))
                    .userTwoId(Math.max(sender, receiver))
                    .lastMessage(dto.getContent())
                    .lastSentAt(LocalDateTime.now())
                    .build();
            conversation = conversationRepository.save(conversation);
            conversationRepository.flush();
        }

        Message msg = Message.builder()
                .conversation(conversation)
                .senderId(sender)
                .receiverId(receiver)
                .message(dto.getContent())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .messageType(dto.getMessageType())
                .build();

        msg = messageRepository.save(msg);
        messageRepository.flush();

        if (conversation.getMessages() != null) {
            conversation.getMessages().add(msg);
        }

        conversation.setLastMessage(dto.getContent());
        conversation.setLastSentAt(msg.getSentAt());
        conversationRepository.save(conversation);
    }

}
