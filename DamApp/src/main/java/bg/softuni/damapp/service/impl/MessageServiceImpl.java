package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void sendMessage(MessageDTO messageDTO) throws UnauthorizedException {
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
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return messageRepository
                .findMessagesByConversationId(conversationId)
                .stream()
                .map(message -> convertToDTO(message, currentUser.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markMessagesAsRead(UUID conversationId, UUID userId) {
        logger.info("Marking messages as read for user: {}", userId);
        List<Message> messages = messageRepository.findMessagesByConversationIdRecipientIdAndIsRead(conversationId, userId, false);
        logger.info("Found {} unread messages for user {}", messages.size(), userId);
        for (Message message : messages) {
            if (!message.isRead()) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }

        logger.info("Updated {} messages to read status for user {}", messages.size(), userId);
    }

    @Override
    public int getUnreadMessageCount(UUID userId) {
        int count = messageRepository.countByRecipientIdAndIsRead(userId, false);
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
    @Transactional
    public void replyToMessage(UUID conversationId, String replyContent) {
        Optional<User> user = getCurrentUser();

        if (user.isPresent()) {
            UUID senderId = user.get().getId();
            Optional<Conversation> conversation = conversationRepository.findById(conversationId);

            if (conversation.isPresent()) {
                UUID recipientId = (conversation.get().getSenderId().equals(senderId))
                        ? conversation.get().getRecipientId()
                        : conversation.get().getSenderId();

                Message newMessage = new Message();
                newMessage.setConversationId(conversationId);
                newMessage.setContent(replyContent);
                newMessage.setCreatedDate(LocalDateTime.now());
                newMessage.setSender(user.get());
                newMessage.setAdvertisement(conversation.get().getAdvertisementId());
                newMessage.setRecipient(userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found")));

                messageRepository.save(newMessage);
            } else {
                throw new RuntimeException("Conversation not found");
            }
        } else {
            throw new RuntimeException("Current user not found");
        }
    }

    @Override
    @Transactional
    public void deleteMessagesByAdvertisementId(UUID advertisementId) {
        messageRepository.deleteByAdvertisementId(advertisementId);
    }

    @Override
    @Transactional
    public void deleteMessagesBySenderIdAndRecipientId(UUID userId) {
        messageRepository.deleteBySenderId(userId);
        messageRepository.deleteByRecipientId(userId);
    }

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(principal.getUsername());
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

    private static MessageDTO convertToDTO(Message message, UUID currentUser) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSender(message.getSender());
        messageDTO.setRecipient(message.getRecipient());
        messageDTO.setAdvertisementId(message.getAdvertisement());
        messageDTO.setContent(message.getContent());
        messageDTO.setCreatedDate(message.getCreatedDate());
        messageDTO.setRead(message.isRead());
        messageDTO.setReceived(message.getRecipient().getId().equals(currentUser));

        return messageDTO;
    }
}
