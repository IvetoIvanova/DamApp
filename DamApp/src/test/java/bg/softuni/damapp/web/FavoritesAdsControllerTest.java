package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.enums.AdType;
import bg.softuni.damapp.model.enums.Category;
import bg.softuni.damapp.service.AdvertisementService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritesAdsController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class FavoritesAdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private AdvertisementService advertisementService;

    @Mock
    private UserDetails userDetails;

    private final UUID advertisementId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("user@example.com");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testShowFavorites() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("user@example.com");

        AdSummaryDTO adSummaryDTO = new AdSummaryDTO(
                UUID.randomUUID(),
                "String title",
                Category.ПЛОДОВЕ,
                "location",
                false,
                AdType.ПОДАРЯВА,
                Collections.singletonList("http://example.com/image.jpg"),
                LocalDateTime.now()
        );

        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);
        when(advertisementService.getFavoriteAdvertisements(userDTO.getId()))
                .thenReturn(Collections.singletonList(adSummaryDTO));

        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorites"))
                .andExpect(model().attributeExists("favoriteAdvertisements"))
                .andExpect(model().attribute("favoriteAdvertisements", Collections.singletonList(adSummaryDTO)));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testRemoveFavorite() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());

        when(userService.findByEmail("user@example.com")).thenReturn(userDTO);

        mockMvc.perform(post("/favorites/remove")
                        .param("advertisementId", advertisementId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/favorites?unreadMessageCount=0"));

        verify(advertisementService).removeFavorite(userDTO.getId(), advertisementId);
    }
}
