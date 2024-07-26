package bg.softuni.damapp.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final MessageSource messageSource;

    public LoginController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {

        if (error != null) {
            String errorMessage = messageSource.getMessage("login_error", null, LocaleContextHolder.getLocale());
            model.addAttribute("loginError", errorMessage);
        }

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", "");
        }
        return "login";
    }
}
