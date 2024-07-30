package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

        Mockito.verify(messageRepository).save(Mockito.any(Message.class));
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
    public void getMessagesForUser_validConversationId_returnsMessages() {
        UUID conversationId = UUID.randomUUID();
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findMessagesByConversationId(conversationId)).thenReturn(messages);

        List<MessageDTO> messageDTOs = messageService.getMessagesForUser(conversationId);

        assertThat(messageDTOs).hasSize(messages.size());
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getContent(), messageDTOs.get(i).getContent());
        }
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

        Mockito.verify(conversationRepository).save(argumentCaptor.capture());
        Conversation savedConversation = argumentCaptor.getValue();
        assertEquals(result, savedConversation.getId());

        assertEquals(senderId, savedConversation.getSenderId());
        assertEquals(recipientId, savedConversation.getRecipientId());
        assertEquals(adId, savedConversation.getAdvertisementId());
    }


    @Test
    public void testGetConversations_returnsCorrectConversations() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID adId = UUID.randomUUID();

        Conversation conversation1 = new Conversation();
        conversation1.setId(UUID.randomUUID());
        conversation1.setSenderId(userId);
        conversation1.setRecipientId(otherUserId);
        conversation1.setAdvertisementId(adId);

        Conversation conversation2 = new Conversation();
        conversation2.setId(UUID.randomUUID());
        conversation2.setSenderId(otherUserId);
        conversation2.setRecipientId(userId);
        conversation2.setAdvertisementId(adId);

        List<Conversation> conversations = List.of(conversation1, conversation2);

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
                "123-456-7890",
                LocalDateTime.now(),
                UUID.randomUUID()
        );

        User otherUser = new User();
        otherUser.setId(otherUserId);
        otherUser.setFirstName("Other User");

        when(conversationRepository.findBySenderIdOrRecipientId(userId, userId)).thenReturn(conversations);
        when(advertisementService.getAdDetails(adId)).thenReturn(adDetails);
        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(otherUser));

        List<ConversationDTO> conversationDTOs = messageService.getConversations(userId);

        assertEquals(2, conversationDTOs.size());
    }

    @Test
    public void testGetConversations_handlesEmptyConversationList() {
        UUID userId = UUID.randomUUID();
        when(conversationRepository.findBySenderIdOrRecipientId(userId, userId)).thenReturn(new ArrayList<>());

        List<ConversationDTO> conversationDTOs = messageService.getConversations(userId);

        assertEquals(0, conversationDTOs.size());
    }

    @Test
    public void testGetConversations_throwsUnauthorizedException() throws UnauthorizedException {
        UUID userId = UUID.randomUUID();
        when(conversationRepository.findBySenderIdOrRecipientId(userId, userId)).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> messageService.getConversations(userId));
    }

}
