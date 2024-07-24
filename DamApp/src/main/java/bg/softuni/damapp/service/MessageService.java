package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.dto.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void sendMessage(MessageDTO messageDTO);

    List<MessageDTO> getMessagesForUser(UUID userId);

    void markMessagesAsRead(UUID conversationId, UUID userId);

    int getUnreadMessageCount(UUID userId);

    void cleanUpOldMessages();

    UUID getOrCreateConversationId(UUID senderId, UUID recipientId, UUID advertisementId);

    List<ConversationDTO> getConversations(UUID userId);
}
