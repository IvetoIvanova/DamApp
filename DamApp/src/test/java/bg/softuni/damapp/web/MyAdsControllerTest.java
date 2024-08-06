package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyAdsController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class MyAdsControllerTest {

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

    @Mock
    private UserDetails userDetails;

    private final UUID adId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("user@example.com");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testGetMyAds() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);
        when(advertisementService.getMyAds(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/my-advertisements"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-advertisements"))
                .andExpect(model().attribute("myAds", Collections.emptyList()));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testReserveAd() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        mockMvc.perform(post("/my-advertisements/reserve-ad/{id}", adId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/my-advertisements?unreadMessageCount=0"));

        verify(advertisementService).reserveAdvertisement(adId, userId);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testUnreserveAd() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        mockMvc.perform(post("/my-advertisements/unreserve-ad/{id}", adId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/my-advertisements?unreadMessageCount=0"));

        verify(advertisementService).unreserveAdvertisement(adId, userId);
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testDeleteAd() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        mockMvc.perform(delete("/my-advertisements/delete-ad/{id}", adId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/my-advertisements?unreadMessageCount=0"));

        verify(conversationService).deleteConversationsByAdvertisementId(adId);
        verify(messageService).deleteMessagesByAdvertisementId(adId);
        verify(advertisementService).deleteAdvertisement(adId, userId);
        verify(advertisementService).removeFromFavorites(adId);
    }
}
