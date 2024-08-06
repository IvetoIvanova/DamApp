package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdsControllerTest {

    @InjectMocks
    private AdsController adsController;

    @Mock
    private AdvertisementService advertisementService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAdsWithoutCategory() {
        List<AdSummaryDTO> ads = new ArrayList<>();
        when(advertisementService.getAllAds()).thenReturn(ads);

        String viewName = adsController.getAllAds(model, null);

        assertEquals("list-advertisements", viewName);
        verify(model).addAttribute("allAds", ads);
    }

    @Test
    public void testGetAllAdsWithCategory() throws UnsupportedEncodingException {
        String category = "electronics";
        String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8.name());
        List<AdSummaryDTO> ads = new ArrayList<>();
        when(advertisementService.getAdsByCategory(encodedCategory)).thenReturn(ads);

        String viewName = adsController.getAllAds(model, category);

        assertEquals("list-advertisements", viewName);
        verify(model).addAttribute("allAds", ads);
    }

    @Test
    public void testGetAllAdsWithCategoryException() {
        String category = "invalid category";
        List<AdSummaryDTO> ads = new ArrayList<>();
        when(advertisementService.getAdsByCategory(any())).thenReturn(ads);

        String viewName = adsController.getAllAds(model, category);

        assertEquals("list-advertisements", viewName);
        verify(model).addAttribute("allAds", ads);
    }
}
