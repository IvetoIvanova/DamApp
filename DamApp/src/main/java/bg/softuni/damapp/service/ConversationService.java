package bg.softuni.damapp.service;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.ConversationDTO;

import java.util.List;
import java.util.UUID;

public interface ConversationService {
    void deleteConversation(UUID conversationId);

    ConversationDTO getConversationById(UUID conversationId);

    List<ConversationDTO> getActiveConversationsForUser(UUID userId) throws UnauthorizedException;

    void deleteConversationsByAdvertisementId(UUID advertisementId);
}
