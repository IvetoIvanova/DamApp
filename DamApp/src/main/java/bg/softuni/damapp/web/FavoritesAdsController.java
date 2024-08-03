package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.AdSummaryDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/favorites")
public class FavoritesAdsController {

    private final UserService userService;
    private final AdvertisementService advertisementService;

    public FavoritesAdsController(UserService userService, AdvertisementService advertisementService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public String showFavorites(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.findByEmail(userDetails.getUsername());
        List<AdSummaryDTO> favoriteAds = advertisementService.getFavoriteAdvertisements(user.getId());
        model.addAttribute("favoriteAdvertisements", favoriteAds);
        return "favorites";
    }

    @PostMapping("/add")
    public String addToFavorites(@RequestParam UUID advertisementId, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.findByEmail(userDetails.getUsername());
        AdSummaryDTO adDetails = advertisementService.getAdvertisementById(advertisementId);
        advertisementService.addFavorite(user.getId(), adDetails.id());
        return "redirect:/advertisement-add/" + advertisementId;
    }

    @PostMapping("/remove")
    public String removeFavorite(@RequestParam UUID advertisementId, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.findByEmail(userDetails.getUsername());
        advertisementService.removeFavorite(user.getId(), advertisementId);
        return "redirect:/favorites";
    }
}
