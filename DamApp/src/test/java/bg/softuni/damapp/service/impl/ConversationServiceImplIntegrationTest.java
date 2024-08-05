package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.repository.ConversationRepository;
import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ConversationServiceImplIntegrationTest {

    @Mock
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
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
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
    @WithMockUser(username = "test@example.com")
    public void testDeleteConversationsByAdvertisementId() {
        UUID advertisementId = UUID.randomUUID();

        conversationService.deleteConversationsByAdvertisementId(advertisementId);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testDeleteConversationsBySenderIdAndRecipientId() {
        UUID userId = UUID.randomUUID();

        conversationService.deleteConversationsBySenderIdAndRecipientId(userId);
    }
}
