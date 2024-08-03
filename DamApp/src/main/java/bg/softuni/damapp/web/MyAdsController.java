package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
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
    private final ConversationService conversationService;
    private final MessageService messageService;

    public MyAdsController(UserService userService, AdvertisementService advertisementService, ConversationService conversationService, MessageService messageService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @GetMapping
    public String getMyAds(Model model, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedException {
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

        conversationService.deleteConversationsByAdvertisementId(id);
        messageService.deleteMessagesByAdvertisementId(id);
        advertisementService.deleteAdvertisement(id, user.getId());
        advertisementService.removeFromFavorites(id);

        return "redirect:/my-advertisements";
    }
}
