package bg.softuni.damapp.service;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void sendMessage(MessageDTO messageDTO) throws UnauthorizedException;

    List<MessageDTO> getMessagesForUser(UUID userId);

    void markMessagesAsRead(UUID conversationId, UUID userId);

    int getUnreadMessageCount(UUID userId);

    void cleanUpOldMessages();

    UUID getOrCreateConversationId(UUID senderId, UUID recipientId, UUID advertisementId);

    void replyToMessage(UUID messageId, String replyContent);

    void deleteMessagesByAdvertisementId(UUID advertisementId);

    void deleteMessagesBySenderIdAndRecipientId(UUID userId);
}
