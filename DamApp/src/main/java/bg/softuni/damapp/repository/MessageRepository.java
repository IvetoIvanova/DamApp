package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId ORDER BY m.createdDate ASC")
    List<Message> findMessagesByConversationId(@Param("conversationId") UUID conversationId);

    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId AND m.recipient.id = :userId AND m.isRead = :isRead")
    List<Message> findMessagesByConversationIdRecipientIdAndIsRead(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId,
            @Param("isRead") boolean isRead
    );

    int deleteByCreatedDateBefore(LocalDateTime createdDate);

    void deleteByConversationId(UUID conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :recipientId AND m.isRead = :isRead")
    int countByRecipientIdAndIsRead(@Param("recipientId") UUID recipientId, @Param("isRead") boolean isRead);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversationId = :conversationId AND m.recipient.id = :recipientId AND m.isRead = :isRead")
    int countByConversationIdAndRecipientIdAndIsRead(
            @Param("conversationId") UUID conversationId,
            @Param("recipientId") UUID recipientId,
            @Param("isRead") boolean isRead);

    void deleteByAdvertisementId(UUID advertisementId);

    void deleteBySenderId(UUID senderId);

    void deleteByRecipientId(UUID senderId);
}
