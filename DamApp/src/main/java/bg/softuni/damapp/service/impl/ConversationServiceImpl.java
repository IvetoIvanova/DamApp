package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.entity.Conversation;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AdvertisementService advertisementService;

    public ConversationServiceImpl(ConversationRepository conversationRepository, MessageRepository messageRepository, UserRepository userRepository, AdvertisementService advertisementService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.advertisementService = advertisementService;
    }

    @Override
    @Transactional
    public void deleteConversation(UUID conversationId) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if (conversation.isPresent()) {
            Conversation conv = conversation.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            Optional<User> optionalUser = userRepository.findByEmail(principal.getUsername());

            if (optionalUser.isPresent()) {
                if (conv.getSenderId().equals(optionalUser.get().getId())) {
                    conv.setSenderDeleted(true);
                } else if (conv.getRecipientId().equals(optionalUser.get().getId())) {
                    conv.setRecipientDeleted(true);
                }
            }

            conversationRepository.save(conv);

            if (conv.isSenderDeleted() && conv.isRecipientDeleted()) {
                messageRepository.deleteByConversationId(conversationId);
                conversationRepository.deleteById(conversationId);
            }

        } else {
            throw new RuntimeException("Conversation not found");
        }
    }

    @Override
    public ConversationDTO getConversationById(UUID conversationId) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);

        if (conversation.isPresent()) {
            Conversation conv = conversation.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            Optional<User> optionalUser = userRepository.findByEmail(principal.getUsername());

            if (optionalUser.isPresent()) {
                if ((conv.getSenderId().equals(optionalUser.get().getId()) && conv.isSenderDeleted()) ||
                        (conv.getRecipientId().equals(optionalUser.get().getId()) && conv.isRecipientDeleted())) {
                    return null;
                }
            }

            return convertToDTO(conv);
        } else {
            throw new EntityNotFoundException("Conversation not found");
        }
    }

    @Override
    public List<ConversationDTO> getActiveConversationsForUser(UUID userId) throws UnauthorizedException {
        List<Conversation> conversations = conversationRepository.findActiveConversationsByUserId(userId);
        List<ConversationDTO> conversationDTOs = new ArrayList<>();

        for (Conversation conversation : conversations) {
            if ((conversation.getSenderId().equals(userId) && conversation.isSenderDeleted()) ||
                    (conversation.getRecipientId().equals(userId) && conversation.isRecipientDeleted())) {
                continue;
            }

            ConversationDTO dto = new ConversationDTO();
            dto.setConversationId(conversation.getId());
            dto.setAdvertisementId(conversation.getAdvertisementId());

            AdDetailsDTO advertisement = advertisementService.getAdDetails(conversation.getAdvertisementId());
            dto.setAdvertisementTitle(advertisement.title());

            UUID otherParticipantId = conversation.getSenderId().equals(userId) ? conversation.getRecipientId() : conversation.getSenderId();
            User otherParticipant = userRepository.findById(otherParticipantId).orElse(null);

            if (otherParticipant != null) {
                dto.setOtherParticipantId(otherParticipant.getId());
                dto.setOtherParticipantName(otherParticipant.getFirstName() + " " + otherParticipant.getLastName());
            }

            int unreadCount = messageRepository.countByConversationIdAndRecipientIdAndIsRead(conversation.getId(), userId, false);
            dto.setUnreadMessageCount(unreadCount);

            conversationDTOs.add(dto);
        }

        return conversationDTOs;
    }

    @Override
    @Transactional
    public void deleteConversationsByAdvertisementId(UUID advertisementId) {
        conversationRepository.deleteByAdvertisementId(advertisementId);
    }

    @Override
    @Transactional
    public void deleteConversationsBySenderIdAndRecipientId(UUID userId) {
        conversationRepository.deleteBySenderId(userId);
        conversationRepository.deleteByRecipientId(userId);
    }

    private static ConversationDTO convertToDTO(Conversation conversation) {
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConversationId(conversation.getId());
        conversationDTO.setRecipientId(conversation.getRecipientId());
        conversationDTO.setAdvertisementId(conversation.getAdvertisementId());
        conversationDTO.setOtherParticipantId(conversation.getRecipientId());

        return conversationDTO;
    }
}
