package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.service.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/list-advertisements")
public class AdsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);

    private final AdvertisementService advertisementService;

    public AdsController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public String getAllAds(Model model, @RequestParam(name = "category", required = false) String category) {
        List<AdSummaryDTO> ads;

        if (category == null) {
            ads = advertisementService.getAllAds();
        } else {
            try {
                category = URLEncoder.encode(category, StandardCharsets.UTF_8.name());
                ads = advertisementService.getAdsByCategory(category);
            } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                e.printStackTrace();
                ads = new ArrayList<>();
            }
        }

        model.addAttribute("allAds", ads);
        return "list-advertisements";
    }
}
