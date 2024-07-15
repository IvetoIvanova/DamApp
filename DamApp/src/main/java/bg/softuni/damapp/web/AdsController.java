package bg.softuni.damapp.web;

import bg.softuni.damapp.service.AdvertisementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/list-advertisements")
public class AdsController {

    private final AdvertisementService advertisementService;

    public AdsController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public String getAllAds(Model model) {
        model.addAttribute("allAds", advertisementService.getAllAds());
        return "list-advertisements";
    }
}
