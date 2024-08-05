package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.entity.Conversation;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversationServiceImplTest {

    private ConversationServiceImpl conversationService;
    @Mock
    private ConversationRepository conversationRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvertisementService advertisementService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserDetails userDetails;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        conversationService = new ConversationServiceImpl(
                conversationRepository,
                messageRepository,
                userRepository,
                advertisementService
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testDeleteConversation_Success_SenderDeletes() {
        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        User user = new User();
        user.setId(senderId);
        userRepository.save(user);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(user.getId());
        conversation.setRecipientId(UUID.randomUUID());
        conversation.setAdvertisementId(UUID.randomUUID());
        conversationRepository.save(conversation);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        Optional<Conversation> foundConversation = conversationRepository.findById(conversationId);
        assertTrue(foundConversation.isPresent());
        assertEquals(conversationId, foundConversation.get().getId());

        conversationService.deleteConversation(conversationId);

        assertTrue(conversation.isSenderDeleted());
        assertFalse(conversation.isRecipientDeleted());
        verify(messageRepository, never()).deleteByConversationId(conversationId);
        verify(conversationRepository, never()).deleteById(conversationId);
    }

    @Test
    void testDeleteConversation_ConversationNotFound() {
        UUID conversationId = UUID.randomUUID();

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            conversationService.deleteConversation(conversationId);
        });

        assertEquals("Conversation not found", thrown.getMessage());
        verify(conversationRepository, never()).save(any());
        verify(messageRepository, never()).deleteByConversationId(any());
        verify(conversationRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteConversation_Success_RecipientDeletes() {
        UUID conversationId = UUID.randomUUID();
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(UUID.randomUUID());
        conversation.setRecipientId(UUID.randomUUID());

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(userDetails.getUsername()).thenReturn("user@example.com");
        User user = new User();
        user.setId(conversation.getRecipientId());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        conversationService.deleteConversation(conversationId);

        assertFalse(conversation.isSenderDeleted());
        assertTrue(conversation.isRecipientDeleted());
        verify(conversationRepository).save(conversation);
        verify(messageRepository, never()).deleteByConversationId(conversationId);
        verify(conversationRepository, never()).deleteById(conversationId);
    }

    @Test
    void testDeleteConversation_Success_BothDeleted() {
        UUID conversationId = UUID.randomUUID();
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(UUID.randomUUID());
        conversation.setRecipientId(UUID.randomUUID());

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        User senderUser = new User();
        senderUser.setId(conversation.getSenderId());
        User recipientUser = new User();
        recipientUser.setId(conversation.getRecipientId());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(senderUser));

        conversation.setRecipientDeleted(true);
        conversation.setSenderDeleted(true);

        conversationService.deleteConversation(conversationId);

        verify(messageRepository).deleteByConversationId(conversationId);
        verify(conversationRepository).deleteById(conversationId);
    }

    @Test
    void testGetConversationById_Success() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(userId);
        conversation.setRecipientId(UUID.randomUUID());
        conversation.setSenderDeleted(false);
        conversation.setRecipientDeleted(false);

        User user = new User();
        user.setId(userId);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        ConversationDTO result = conversationService.getConversationById(conversationId);

        assertNotNull(result);
        assertEquals(conversationId, result.getConversationId());
    }


    @Test
    void testGetConversationById_ConversationNotFound() {
        UUID conversationId = UUID.randomUUID();
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> conversationService.getConversationById(conversationId));
    }

    @Test
    void testGetConversationById_SenderDeleted() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UUID conversationId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(senderId);
        conversation.setRecipientId(UUID.randomUUID());
        conversation.setSenderDeleted(true);
        conversation.setRecipientDeleted(false);

        User user = new User();
        user.setId(senderId);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        ConversationDTO result = conversationService.getConversationById(conversationId);

        assertNull(result);
    }

    @Test
    public void testGetActiveConversationsForUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID advertisementId = UUID.randomUUID();

        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        conversation.setSenderId(userId);
        conversation.setRecipientId(otherUserId);
        conversation.setAdvertisementId(advertisementId);
        conversation.setSenderDeleted(false);
        conversation.setRecipientDeleted(false);

        List<Conversation> conversations = new ArrayList<>();
        conversations.add(conversation);

        when(conversationRepository.findActiveConversationsByUserId(userId)).thenReturn(conversations);
        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(new User()));
        when(advertisementService.getAdDetails(advertisementId)).thenReturn(new AdDetailsDTO(
                UUID.randomUUID(),
                "Test Advertisement",
                "Test Description",
                Category.ДРУГИ,
                1,
                "Sample Location",
                false,
                AdType.ПОДАРЯВА,
                List.of("http://example.com/image1.jpg"),
                "123-456-7890",
                LocalDateTime.now(),
                UUID.randomUUID()));

        List<ConversationDTO> result = conversationService.getActiveConversationsForUser(userId);

        assertEquals(1, result.size());
        ConversationDTO dto = result.get(0);
        assertEquals(conversation.getId(), dto.getConversationId());
        assertEquals("Test Advertisement", dto.getAdvertisementTitle());
    }

    @Test
    public void testGetActiveConversationsForUser_UserDeleted() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID advertisementId = UUID.randomUUID();

        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        conversation.setSenderId(userId);
        conversation.setRecipientId(otherUserId);
        conversation.setAdvertisementId(advertisementId);
        conversation.setSenderDeleted(true);

        List<Conversation> conversations = new ArrayList<>();
        conversations.add(conversation);

        when(conversationRepository.findActiveConversationsByUserId(userId)).thenReturn(conversations);

        List<ConversationDTO> result = conversationService.getActiveConversationsForUser(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    public void testDeleteConversationsByAdvertisementId() {
        UUID advertisementId = UUID.randomUUID();

        conversationService.deleteConversationsByAdvertisementId(advertisementId);

        verify(conversationRepository, times(1)).deleteByAdvertisementId(advertisementId);
    }

    @Test
    @Transactional
    public void testDeleteConversationsBySenderIdAndRecipientId() {
        UUID userId = UUID.randomUUID();

        conversationService.deleteConversationsBySenderIdAndRecipientId(userId);

        verify(conversationRepository, times(1)).deleteBySenderId(userId);
        verify(conversationRepository, times(1)).deleteByRecipientId(userId);
    }
}
