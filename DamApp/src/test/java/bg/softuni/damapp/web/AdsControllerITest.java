package bg.softuni.damapp.web;

import bg.softuni.damapp.config.TestSecurityConfig;
import bg.softuni.damapp.config.TestWConfig;
import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(AdsController.class)
@Import({TestWConfig.class, TestSecurityConfig.class})
public class AdsControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAdsWithoutCategory() throws Exception {
        List<AdSummaryDTO> ads = new ArrayList<>();
        when(advertisementService.getAllAds()).thenReturn(ads);

        mockMvc.perform(MockMvcRequestBuilders.get("/list-advertisements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("list-advertisements"))
                .andExpect(MockMvcResultMatchers.model().attribute("allAds", ads));
    }

    @Test
    public void testGetAllAdsWithCategory() throws Exception {
        String category = "electronics";
        String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8.name());
        List<AdSummaryDTO> ads = new ArrayList<>();
        when(advertisementService.getAdsByCategory(encodedCategory)).thenReturn(ads);

        mockMvc.perform(MockMvcRequestBuilders.get("/list-advertisements")
                        .param("category", category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("list-advertisements"))
                .andExpect(MockMvcResultMatchers.model().attribute("allAds", ads));
    }

}
