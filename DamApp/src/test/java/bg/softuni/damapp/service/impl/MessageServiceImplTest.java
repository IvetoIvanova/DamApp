package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.MessageDTO;
import bg.softuni.damapp.model.entity.Conversation;
import bg.softuni.damapp.model.entity.Message;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdvertisementService advertisementService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(
                messageRepository,
                userRepository,
                conversationRepository,
                advertisementService
        );
    }

    @Test
    public void sendMessage_validMessageDTO_createsMessage() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UUID adId = UUID.randomUUID();
        User sender = new User();
        sender.setId(senderId);
        User recipient = new User();
        recipient.setId(recipientId);
        AdDetailsDTO adDetails = new AdDetailsDTO(
                UUID.randomUUID(),
                "Sample Title",
                "Sample Description",
                Category.ДРУГИ,
                10,
                "Sample Location",
                false,
                AdType.ПОДАРЯВА,
                List.of("http://example.com/image1.jpg"),
                "+359887899885",
                LocalDateTime.now(),
                UUID.randomUUID()
        );

        Mockito.when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        Mockito.when(advertisementService.getAdDetails(adId)).thenReturn(adDetails);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setConversationId(UUID.randomUUID());
        messageDTO.setSender(sender);
        messageDTO.setRecipient(recipient);
        messageDTO.setAdvertisementId(adId);
        messageDTO.setContent("Test message");

        messageService.sendMessage(messageDTO);

        verify(messageRepository).save(any(Message.class));
    }

    @Test
    public void testSendMessage_UserNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UUID adId = UUID.randomUUID();

        MessageDTO messageDTO = new MessageDTO();
        User user1 = new User();
        user1.setId(senderId);
        messageDTO.setSender(user1);
        User user2 = new User();
        user2.setId(recipientId);
        messageDTO.setAdvertisementId(adId);
        messageDTO.setContent("Test message");
        messageDTO.setConversationId(UUID.randomUUID());

        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> messageService.sendMessage(messageDTO));
    }

    @Test
    public void testSendMessage_NullConversationId() {
        UUID adId = UUID.randomUUID();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(new User());
        messageDTO.setRecipient(new User());
        messageDTO.setAdvertisementId(adId);
        messageDTO.setContent("Test message");
        messageDTO.setConversationId(null);

        assertThrows(NoSuchElementException.class, () -> {
            messageService.sendMessage(messageDTO);
        });
    }

    @Test
    public void testGetMessagesForUser() {
        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user1 = new User();
        user1.setId(userId);
        User user2 = new User();
        user2.setId(userId);

        Message message = new Message();
        message.setSender(user1);
        message.setRecipient(user2);
        message.setId(UUID.randomUUID());
        message.setContent("Hello");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(messageRepository.findMessagesByConversationId(conversationId)).thenReturn(List.of(message));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        List<MessageDTO> result = messageService.getMessagesForUser(conversationId);
        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getContent());
    }

    @Test
    public void getOrCreateConversationId_existingConversation_returnsId() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UUID adId = UUID.randomUUID();
        UUID expectedConversationId = UUID.randomUUID();

        Conversation conversation = new Conversation();
        conversation.setId(expectedConversationId);
        conversation.setSenderId(senderId);
        conversation.setRecipientId(recipientId);
        conversation.setAdvertisementId(adId);

        when(conversationRepository.findBySenderIdAndRecipientIdAndAdvertisementId(senderId, recipientId, adId))
                .thenReturn(Optional.of(conversation));

        UUID result = messageService.getOrCreateConversationId(senderId, recipientId, adId);

        assertEquals(expectedConversationId, result);
    }

    @Test
    public void getOrCreateConversationId_newConversation_createsAndReturnsId() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UUID adId = UUID.randomUUID();
        Mockito.when(conversationRepository.findBySenderIdAndRecipientIdAndAdvertisementId(senderId, recipientId, adId)).thenReturn(Optional.empty());

        ArgumentCaptor<Conversation> argumentCaptor = ArgumentCaptor.forClass(Conversation.class);

        UUID result = messageService.getOrCreateConversationId(senderId, recipientId, adId);

        verify(conversationRepository).save(argumentCaptor.capture());
        Conversation savedConversation = argumentCaptor.getValue();
        assertEquals(result, savedConversation.getId());

        assertEquals(senderId, savedConversation.getSenderId());
        assertEquals(recipientId, savedConversation.getRecipientId());
        assertEquals(adId, savedConversation.getAdvertisementId());
    }

    @Test
    @Transactional
    public void testMarkMessagesAsRead() {
        UUID conversationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Message unreadMessage = new Message();
        unreadMessage.setId(UUID.randomUUID());
        unreadMessage.setRead(false);

        when(messageRepository.findMessagesByConversationIdRecipientIdAndIsRead(conversationId, userId, false))
                .thenReturn(List.of(unreadMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(unreadMessage);

        messageService.markMessagesAsRead(conversationId, userId);

        verify(messageRepository).save(unreadMessage);
        assertTrue(unreadMessage.isRead());
    }

    @Test
    public void testGetUnreadMessageCount() {
        UUID userId = UUID.randomUUID();
        when(messageRepository.countByRecipientIdAndIsRead(userId, false)).thenReturn(5);

        int count = messageService.getUnreadMessageCount(userId);

        assertEquals(5, count);
        verify(messageRepository, times(1)).countByRecipientIdAndIsRead(userId, false);
    }

    @Test
    public void testCleanUpOldMessages() {
        LocalDate daysAgo = LocalDate.now().minusDays(180);
        when(messageRepository.deleteByCreatedDateBefore(daysAgo.atStartOfDay())).thenReturn(10);

        messageService.cleanUpOldMessages();

        verify(messageRepository, times(1)).deleteByCreatedDateBefore(daysAgo.atStartOfDay());
    }

    @Test
    @Transactional
    public void testDeleteMessagesByAdvertisementId() {
        UUID advertisementId = UUID.randomUUID();

        messageService.deleteMessagesByAdvertisementId(advertisementId);

        verify(messageRepository, times(1)).deleteByAdvertisementId(advertisementId);
    }

    @Test
    @Transactional
    public void testDeleteMessagesBySenderIdAndRecipientId() {
        UUID userId = UUID.randomUUID();

        messageService.deleteMessagesBySenderIdAndRecipientId(userId);

        verify(messageRepository, times(1)).deleteBySenderId(userId);
        verify(messageRepository, times(1)).deleteByRecipientId(userId);
    }

    @Test
    public void testReplyToMessage() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UUID conversationId = UUID.randomUUID();
        String replyContent = "Test reply";
        UUID userId = UUID.randomUUID();

        User currentUser = new User();
        currentUser.setId(userId);
        userRepository.save(currentUser);

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setSenderId(userId);
        conversation.setRecipientId(UUID.randomUUID());
        conversation.setAdvertisementId(UUID.randomUUID());

        User recipient = new User();
        recipient.setId(conversation.getRecipientId());

        Message newMessage = new Message();
        newMessage.setConversationId(conversationId);
        newMessage.setContent(replyContent);
        newMessage.setCreatedDate(LocalDateTime.now());
        newMessage.setSender(currentUser);
        newMessage.setAdvertisement(conversation.getAdvertisementId());
        newMessage.setRecipient(recipient);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(recipient));
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        messageService.replyToMessage(conversationId, replyContent);

        verify(messageRepository, times(1)).save(argThat(message -> {
            return message.getConversationId().equals(conversationId)
                    && message.getContent().equals(replyContent)
                    && message.getSender().equals(currentUser)
                    && message.getRecipient().equals(recipient);
        }));
    }

    @Test
    public void testReplyToMessage_ConversationNotFound() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UUID conversationId = UUID.randomUUID();
        String replyContent = "Test reply";

        User currentUser = new User();
        currentUser.setId(UUID.randomUUID());
        userRepository.save(currentUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(currentUser));
        when(conversationRepository.findById(conversationId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            messageService.replyToMessage(conversationId, replyContent);
        });
        assertEquals("Conversation not found", thrown.getMessage());
    }
}