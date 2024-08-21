package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.service.ContactService;
import bg.softuni.damapp.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/contact-form")
public class ContactController {

    private final EmailService emailService;
    private final ContactService contactService;
    private final MessageSource messageSource;

    public ContactController(EmailService emailService, ContactService contactService, MessageSource messageSource) {
        this.emailService = emailService;
        this.contactService = contactService;
        this.messageSource = messageSource;
    }

    @ModelAttribute("contactFormData")
    public ContactFormDto contactFormDto() {
        return new ContactFormDto();
    }

    @GetMapping
    public String showContactPage() {
        return "contact-form";
    }

    @PostMapping
    public String sendMessage(@Valid ContactFormDto contactForm,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactFormData", bindingResult);
            redirectAttributes.addFlashAttribute("contactFormData", contactForm);
            return "redirect:/contact-form";
        }

        try {
            contactService.saveMessage(contactForm);
            emailService.sendEmailToAdmin(contactForm);
            String successMessage = messageSource.getMessage("success_message", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (MailException e) {
            String errorMessage = messageSource.getMessage("error_message", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            e.printStackTrace();
        }

        return "redirect:/contact-form";
    }
}
