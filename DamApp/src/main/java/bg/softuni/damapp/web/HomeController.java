package bg.softuni.damapp.web;

import bg.softuni.damapp.model.user.DamUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails,
                       Model model) {

        if (userDetails instanceof DamUserDetails harvestShareUserDetails) {
            model.addAttribute("welcomeMessage", harvestShareUserDetails.getFullName());
        }

        return "index";
    }
}
