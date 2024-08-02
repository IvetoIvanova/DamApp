package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findBySenderIdAndRecipientIdAndAdvertisementId(UUID senderId, UUID recipientId, UUID advertisementId);

    List<Conversation> findBySenderIdOrRecipientId(UUID userId, UUID userId1);

    void deleteById(UUID id);

    @Query("SELECT c FROM Conversation c WHERE (c.senderId = :userId AND c.senderDeleted = false) OR (c.recipientId = :userId AND c.recipientDeleted = false)")
    List<Conversation> findActiveConversationsByUserId(@Param("userId") UUID userId);
}
