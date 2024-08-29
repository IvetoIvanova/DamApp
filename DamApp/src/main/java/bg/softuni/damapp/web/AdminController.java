package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.RoleAlreadyExistsException;
import bg.softuni.damapp.exception.RoleDoesNotExistsException;
import bg.softuni.damapp.model.dto.ContactMessageDto;
import bg.softuni.damapp.model.dto.ReplyContactMessageDto;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.ContactMessage;
import bg.softuni.damapp.model.entity.Report;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.entity.UserRole;
import bg.softuni.damapp.model.enums.UserRoleEnum;
import bg.softuni.damapp.service.*;
import jakarta.validation.Valid;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final RoleService roleService;
    private final EmailService emailService;
    private final ContactService contactService;
    private final ReportService reportService;

    public AdminController(UserService userService, AdvertisementService advertisementService, RoleService roleService, EmailService emailService, ContactService contactService, ReportService reportService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.roleService = roleService;
        this.emailService = emailService;
        this.contactService = contactService;
        this.reportService = reportService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminDashboard(@RequestParam(value = "email", required = false) String email, Model model) {
        List<UserDTO> users;

        if (email != null && !email.isEmpty()) {
            UserDTO user = userService.findByEmail(email);
            users = user != null ? Collections.singletonList(user) : Collections.emptyList();
        } else {
            users = userService.findAllUsers();
        }

        List<UserRole> allRoles = roleService.findAllRolesOfUser();
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewMessages(Model model) {
        List<ContactMessage> messages = contactService.findAllContactMessages();
        model.addAttribute("messages", messages);
        return "contact-messages";
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewReports(Model model) {
        List<Report> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "admin-reports";
    }

    @PostMapping("/sendReply")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String sendReply(@Valid ReplyContactMessageDto replyMessageDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка при изпращане на отговора. Моля, проверете въведените данни!");
            return "redirect:/admin/contacts";
        }

        try {
            ContactMessageDto contactMessage = contactService.findMessageById(replyMessageDto.getMessageId());
            emailService.sendReply(replyMessageDto.getRecipientEmail(), replyMessageDto.getReplyMessage(), contactMessage);
            redirectAttributes.addFlashAttribute("successMessage", "Отговорът беше изпратен успешно!");
        } catch (MailException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка при изпращане на отговора. Моля, опитайте отново!");
        }
        return "redirect:/admin/contacts";
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addRoleToUser(@PathVariable UUID userId,
                                @RequestParam("role") String roleName,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<User> user = userService.findById(userId);
            user.ifPresent(value -> roleService.addRoleToUser(value, UserRoleEnum.valueOf(roleName)));
            return "redirect:/admin";
        } catch (RoleAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/users/{userId}/remove-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String removeRoleFromUser(@PathVariable UUID userId,
                                     @RequestParam("role") String roleName,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<User> user = userService.findById(userId);
            user.ifPresent(u -> roleService.removeRoleFromUser(u, UserRoleEnum.valueOf(roleName)));
            return "redirect:/admin";
        } catch (RoleDoesNotExistsException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/users/{userId}/disable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String disableUser(@PathVariable UUID userId,
                              RedirectAttributes redirectAttributes) {
        userService.disableUser(userId);
        String successMessage = "Потребителят е деактивиран.";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admin";
    }

    @PostMapping("/users/{userId}/enable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String enableUser(@PathVariable UUID userId,
                             RedirectAttributes redirectAttributes) {
        userService.enableUser(userId);
        String successMessage = "Потребителят е активиран.";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admin";
    }

//    @DeleteMapping("/delete-ad/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String deleteAd(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        UserDTO user = userService.findByEmail(email);
//
//        advertisementService.deleteAdvertisement(id, user.getId());
//        return "list-advertisements";
//    }

}
