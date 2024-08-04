package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/my-profile")
public class UserProfileController {
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final ConversationService conversationService;
    private final MessageService messageService;

    public UserProfileController(UserService userService, AdvertisementService advertisementService, ConversationService conversationService, MessageService messageService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        UserDTO userDTO = userService.findByEmail(principal.getName());
        model.addAttribute("userData", userDTO);
        return "/my-profile";
    }

    @GetMapping("/profile-deleted")
    public String logoutSuccess() {
        return "profile-deleted";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam("email") String email,
                              RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        try {
            userService.updateEmail(userDTO.getId(), email);
            redirectAttributes.addFlashAttribute("message", "Имейлът е променен успешно.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        try {
            userService.updatePassword(userDTO.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("message", "Паролата е променена успешно.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-profile";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) throws UnauthorizedException {
        UserDTO user = userService.findByEmail(userDetails.getUsername());

        conversationService.deleteConversationsBySenderIdAndRecipientId(user.getId());
        messageService.deleteMessagesBySenderIdAndRecipientId(user.getId());

        List<UUID> advertisementIds = advertisementService.getMyAds(user.getId()).stream()
                .map(AdDetailsDTO::id)
                .toList();

        for (UUID ad : advertisementIds) {
            conversationService.deleteConversationsByAdvertisementId(ad);
            messageService.deleteMessagesByAdvertisementId(ad);

            advertisementService.removeFromFavorites(ad);

            advertisementService.deleteAdvertisement(ad, user.getId());
        }

        try {
            userService.deleteUser(user.getEmail());
            request.logout();
            return "profile-deleted";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/my-profile";
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
