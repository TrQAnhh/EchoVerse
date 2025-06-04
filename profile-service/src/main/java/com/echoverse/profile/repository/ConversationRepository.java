package com.echoverse.profile.repository;

import com.echoverse.profile.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c WHERE " +
            "(c.userOneId = :user1 AND c.userTwoId = :user2) OR " +
            "(c.userOneId = :user2 AND c.userTwoId = :user1)")
    Optional<Conversation> findByUserIds(@Param("user1") Long user1, @Param("user2") Long user2);

    @Query("SELECT c FROM Conversation c WHERE c.userOneId = :userId OR c.userTwoId = :userId ORDER BY c.lastSentAt DESC")
    List<Conversation> findAllByUserId(@Param("userId") Long userId);
}