package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.dto.MessageDTO;
import bg.softuni.damapp.model.entity.Conversation;
import bg.softuni.damapp.model.entity.Message;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.MessageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final AdvertisementService advertisementService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, ConversationRepository conversationRepository, AdvertisementService advertisementService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.advertisementService = advertisementService;
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setSender(userRepository.findById(messageDTO.getSender().getId()).orElseThrow());
        message.setRecipient(userRepository.findById(messageDTO.getRecipient().getId()).orElseThrow());

        AdDetailsDTO advertisement = advertisementService.getAdDetails(messageDTO.getAdvertisementId());
        message.setAdvertisement(advertisement.id());
        message.setRead(false);

        UUID conversationId = getOrCreateConversationId(
                messageDTO.getSender().getId(),
                messageDTO.getRecipient().getId(),
                messageDTO.getAdvertisementId()
        );

        message.setConversationId(conversationId);
        message.setContent(messageDTO.getContent());
        message.setCreatedDate(messageDTO.getCreatedDate());

        if (messageDTO.getConversationId() == null) {
            throw new IllegalArgumentException("Conversation ID cannot be null");
        }

        messageRepository.save(message);
    }

    @Override
    public List<MessageDTO> getMessagesForUser(UUID conversationId) {
        return messageRepository
                .findMessagesByConversationId(conversationId)
                .stream()
                .map(MessageServiceImpl::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markMessagesAsRead(UUID conversationId, UUID userId) {
        logger.info("Marking messages as read for user: {}", userId);
        List<Message> messages = messageRepository.findMessagesByConversationIdRecipientIdAndIsRead(conversationId, userId, false);
        logger.info("Found {} unread messages for user {}", messages.size(), userId);
        for (Message message : messages) {
            message.setRead(true);
        }
        messageRepository.saveAllAndFlush(messages);
        logger.info("Updated {} messages to read status for user {}", messages.size(), userId);
    }

    @Override
    public int getUnreadMessageCount(UUID userId) {
        int count = messageRepository.countUnreadMessagesByUserId(userId);
        logger.info("Unread message count for user {}: {}", userId, count);
        return count;
    }


    @Override
    public void cleanUpOldMessages() {
        LocalDate daysAgo = LocalDate.now().minusDays(180);
        int deletedMessagesCount = messageRepository.deleteByCreatedDateBefore(daysAgo.atStartOfDay());
        LOGGER.info("Deleted " + deletedMessagesCount + " messages older than 180 days.");
    }

    @Override
    public List<ConversationDTO> getConversations(UUID userId) {
        List<Conversation> conversations = conversationRepository.findBySenderIdOrRecipientId(userId, userId);
        List<ConversationDTO> conversationDTOs = new ArrayList<>();

        for (Conversation conversation : conversations) {
            ConversationDTO dto = new ConversationDTO();
            dto.setConversationId(conversation.getId());
            dto.setAdvertisementId(conversation.getAdvertisementId());

            AdDetailsDTO advertisement = advertisementService.getAdDetails(conversation.getAdvertisementId());
            dto.setAdvertisementTitle(advertisement.title());

            UUID otherParticipantId = conversation.getSenderId().equals(userId) ? conversation.getRecipientId() : conversation.getSenderId();
            User otherParticipant = userRepository.findById(otherParticipantId).orElse(null);

            if (otherParticipant != null) {
                dto.setOtherParticipantId(otherParticipant.getId());
                dto.setOtherParticipantName(otherParticipant.getFirstName());
            }

            conversationDTOs.add(dto);
        }

        return conversationDTOs;
    }

    @Override
    public UUID getOrCreateConversationId(UUID senderId, UUID recipientId, UUID advertisementId) {
        Optional<Conversation> conversation = conversationRepository
                .findBySenderIdAndRecipientIdAndAdvertisementId(senderId, recipientId, advertisementId);

        if (conversation.isPresent()) {
            return conversation.get().getId();
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setSenderId(senderId);
            newConversation.setRecipientId(recipientId);
            newConversation.setAdvertisementId(advertisementId);
            newConversation.setId(UUID.randomUUID());
            conversationRepository.save(newConversation);
            return newConversation.getId();
        }
    }

    private static MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSender(message.getSender());
        messageDTO.setRecipient(message.getRecipient());
        messageDTO.setAdvertisementId(message.getAdvertisement());
        messageDTO.setContent(message.getContent());
        messageDTO.setCreatedDate(message.getCreatedDate());
        messageDTO.setRead(message.isRead());
        return messageDTO;
    }
}
