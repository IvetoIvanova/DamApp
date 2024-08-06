package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private ConversationService conversationService;

    @Mock
    private UserDetails userDetails;

    private final UUID conversationId = UUID.randomUUID();
    private final UUID advertisementId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("user@example.com");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testShowConversations() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConversationId(conversationId);

        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);
        when(conversationService.getActiveConversationsForUser(userId))
                .thenReturn(Collections.singletonList(conversationDTO));
        when(messageService.getUnreadMessageCount(userId)).thenReturn(5);

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages"))
                .andExpect(model().attributeExists("conversations"))
                .andExpect(model().attribute("conversations", Collections.singletonList(conversationDTO)))
                .andExpect(model().attribute("unreadCount", 5));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testShowConversationNotFound() throws Exception {
        when(userService.findByEmail("user@example.com")).thenReturn(new UserDTO());
        when(conversationService.getConversationById(conversationId)).thenReturn(null);

        mockMvc.perform(get("/messages/conversation/{conversationId}", conversationId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/messages?unreadMessageCount=0"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteConversation() throws Exception {
        doNothing().when(conversationService).deleteConversation(conversationId);

        mockMvc.perform(get("/messages/conversation/delete/{id}", conversationId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/messages"))
                .andExpect(flash().attribute("message", "Разговорът беше успешно изтрит."));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteConversationFailure() throws Exception {
        doThrow(new RuntimeException()).when(conversationService).deleteConversation(conversationId);

        mockMvc.perform(get("/messages/conversation/delete/{id}", conversationId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/messages"))
                .andExpect(flash().attribute("error", "Възникна грешка при изтриването на разговора."));
    }


    @Test
    @WithMockUser(username = "user@example.com")
    public void testReplyToMessageSuccess() throws Exception {
        mockMvc.perform(post("/messages/reply")
                        .param("conversationId", conversationId.toString())
                        .param("replyContent", "Reply content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/messages"))
                .andExpect(flash().attribute("message", "Съобщението беше успешно изпратено."));

        verify(messageService).replyToMessage(conversationId, "Reply content");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testReplyToMessageFailure() throws Exception {
        doThrow(new RuntimeException()).when(messageService).replyToMessage(any(UUID.class), anyString());

        mockMvc.perform(post("/messages/reply")
                        .param("conversationId", conversationId.toString())
                        .param("replyContent", "Reply content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/messages"))
                .andExpect(flash().attribute("error", "Възникна грешка при изпращането на съобщението."));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testGetUnreadMessageCount() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);
        when(messageService.getUnreadMessageCount(userId)).thenReturn(10);

        mockMvc.perform(get("/messages/unread-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testMarkMessagesAsReadSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        mockMvc.perform(post("/messages/conversation/mark-as-read")
                        .param("conversationId", conversationId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testMarkMessagesAsReadFailure() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);
        doThrow(new RuntimeException()).when(messageService).markMessagesAsRead(conversationId, userId);

        mockMvc.perform(post("/messages/conversation/mark-as-read")
                        .param("conversationId", conversationId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Error marking messages as read"));
    }

}
