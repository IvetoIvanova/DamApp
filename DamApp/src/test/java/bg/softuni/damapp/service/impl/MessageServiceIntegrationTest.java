package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.MessageDTO;
import bg.softuni.damapp.model.entity.Advertisement;
import bg.softuni.damapp.model.entity.Conversation;
import bg.softuni.damapp.model.entity.Message;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.MessageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class MessageServiceIntegrationTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    private User user;
    private Conversation conversation;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setActive(true);
        user.setUuid(UUID.randomUUID());
        userRepository.save(user);

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Test Advertisement");
        advertisement.setId(UUID.randomUUID());

        conversation = new Conversation();
        conversation.setId(UUID.randomUUID());
        conversation.setSenderId(user.getId());
        conversation.setRecipientId(user.getId());
        conversation.setAdvertisementId(advertisement.getId());
        conversationRepository.save(conversation);
    }

    @Test
    @Transactional
    @WithMockUser(username = "test@example.com")
    public void testGetMessagesForUser() {
        Message message = new Message();
        message.setConversationId(conversation.getId());
        message.setContent("Hello");
        message.setSender(user);
        message.setRecipient(user);
        message.setAdvertisement(UUID.randomUUID());
        message.setRead(false);
        message.setCreatedDate(LocalDateTime.now());
        messageRepository.save(message);

        List<MessageDTO> messages = messageService.getMessagesForUser(conversation.getId());
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test
    @Transactional
    public void testMarkMessagesAsRead() {
        Message unreadMessage = new Message();
        unreadMessage.setConversationId(conversation.getId());
        unreadMessage.setContent("Unread");
        unreadMessage.setSender(user);
        unreadMessage.setRecipient(user);
        unreadMessage.setRead(false);
        unreadMessage.setCreatedDate(LocalDateTime.now());
        unreadMessage.setAdvertisement(UUID.randomUUID());
        messageRepository.save(unreadMessage);

        messageService.markMessagesAsRead(conversation.getId(), user.getId());

        Message updatedMessage = messageRepository.findById(unreadMessage.getId()).orElseThrow();
        assertTrue(updatedMessage.isRead());
    }
}
