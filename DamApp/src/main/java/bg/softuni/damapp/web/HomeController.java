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

        if (userDetails instanceof DamUserDetails damUserDetails) {
            model.addAttribute("welcomeMessage", damUserDetails.getFullName());
        }

        return "index";
    }

    @GetMapping("/about-us")
    public String showAboutUsPage() {
        return "about-us";
    }

    @GetMapping("/terms")
    public String showTermsPage() {
        return "terms";
    }

}
