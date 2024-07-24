package bg.softuni.damapp.repository;

import bg.softuni.damapp.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findBySenderIdAndRecipientIdAndAdvertisementId(UUID senderId, UUID recipientId, UUID advertisementId);

    List<Conversation> findBySenderIdOrRecipientId(UUID userId, UUID userId1);
}
