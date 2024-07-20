package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/my-profile")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        UserDTO userDTO = userService.findByEmail(principal.getName());
        model.addAttribute("userData", userDTO);
        return "/my-profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        userService.updateEmail(userDTO.getId(), email);
        redirectAttributes.addFlashAttribute("message", "Email updated successfully");
        return "redirect:/my-profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        userService.updatePassword(userDTO.getId(), password);
        redirectAttributes.addFlashAttribute("message", "Password updated successfully");
        return "redirect:/my-profile";
    }
}
