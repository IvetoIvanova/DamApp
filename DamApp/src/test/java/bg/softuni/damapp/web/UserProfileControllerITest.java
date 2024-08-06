package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserProfileController.class)
@Import(TestWConfig.class)
public class UserProfileControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private ConversationService conversationService;

    @MockBean
    private MessageService messageService;

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    public void deleteAccount_ShouldPerformDeletionAndRedirect() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");
        
        AdDetailsDTO adDetailsDTO = new AdDetailsDTO(UUID.randomUUID(),
                "String title",
                "String description",
                Category.ПЛОДОВЕ,
                1000,
                "String location",
                false,
                AdType.ПОДАРЯВА,
                new ArrayList<>(),
                "+359887777777",
                LocalDateTime.now(),
                UUID.randomUUID());

        when(userService.findByEmail("test@example.com")).thenReturn(userDTO);
        when(advertisementService.getMyAds(userDTO.getId())).thenReturn(Collections.singletonList(adDetailsDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/my-profile/delete-account")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(conversationService).deleteConversationsByAdvertisementId(adDetailsDTO.id());
        verify(messageService).deleteMessagesByAdvertisementId(adDetailsDTO.id());
        verify(conversationService).deleteConversationsBySenderIdAndRecipientId(userDTO.getId());
        verify(messageService).deleteMessagesBySenderIdAndRecipientId(userDTO.getId());

        verify(conversationService).deleteConversationsByAdvertisementId(adDetailsDTO.id());
        verify(messageService).deleteMessagesByAdvertisementId(adDetailsDTO.id());
        verify(advertisementService).removeFromFavorites(adDetailsDTO.id());
        verify(advertisementService).deleteAdvertisement(adDetailsDTO.id(), userDTO.getId());
    }
}
