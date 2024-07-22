package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/reserve-ad/{id}")
    public String reserveAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmail(email);

        advertisementService.reserveAdvertisement(id, user.getId());
        return "redirect:/my-advertisements";
    }

    @PostMapping("/unreserve-ad/{id}")
    public String unreserveAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmail(email);

        advertisementService.unreserveAdvertisement(id, user.getId());
        return "redirect:/my-advertisements";
    }

    @DeleteMapping("/delete-ad/{id}")
    public String deleteAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmail(email);

        advertisementService.deleteAdvertisement(id, user.getId());
        return "redirect:/my-advertisements";
    }
}
