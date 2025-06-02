package com.echoverse.profile.repository;

import com.echoverse.profile.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.conversation.conversationId = :conversationId ORDER BY m.sentAt DESC")
    List<Message> findByConversationId(@Param("conversationId") Long conversationId);
}