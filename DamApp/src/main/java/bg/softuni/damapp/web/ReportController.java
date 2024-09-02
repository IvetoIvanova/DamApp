package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.ReportDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.service.ReportService;
import bg.softuni.damapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/report-form")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping
    public String showReportPage(Model model) {
        ReportDTO reportDTO = reportService.createReportDTOForCurrentUser();
        model.addAttribute("reportDТО", reportDTO);
        return "report-form";
    }

    @PostMapping
    public String submitReport(@Valid @ModelAttribute("reportDТО") ReportDTO reportDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "redirect:/report-form";
        }

        reportService.saveReport(reportDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Репортът е изпратен успешно.");
        return "redirect:/report-form";
    }
}
