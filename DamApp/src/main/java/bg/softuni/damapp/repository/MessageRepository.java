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

//    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.recipient.id = :userId ORDER BY m.createdDate DESC")
//    List<Message> findConversationsByUserId(@Param("userId") UUID userId);

    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId ORDER BY m.createdDate ASC")
    List<Message> findMessagesByConversationId(@Param("conversationId") UUID conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :userId AND m.isRead = false")
    int countUnreadMessagesByUserId(@Param("userId") UUID userId);

    //    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId AND m.recipient.id = :userId")
//    List<Message> findMessagesByConversationIdAndRecipientId(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);
    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId AND m.recipient.id = :userId AND m.isRead = :isRead")
    List<Message> findMessagesByConversationIdRecipientIdAndIsRead(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId,
            @Param("isRead") boolean isRead
    );


    int deleteByCreatedDateBefore(LocalDateTime createdDate);

//    @Query("SELECT m FROM Message m WHERE m.recipient.id = :userId AND m.sender.id = :otherUserId AND m.isRead = false")
//    List<Message> findUnreadMessages(@Param("userId") UUID userId, @Param("otherUserId") UUID otherUserId);
}
