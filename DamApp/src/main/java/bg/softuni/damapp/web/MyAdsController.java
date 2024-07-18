package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-advertisements")
public class MyAdsController {

    private final UserService userService;

    private final AdvertisementService advertisementService;

    public MyAdsController(UserService userService, AdvertisementService advertisementService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public String getMyAds(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmail(email);

        if (!model.containsAttribute("myAds")) {
            model.addAttribute("myAds", advertisementService.getMyAds(user.getId()));
        }
        return "my-advertisements";
    }
}
