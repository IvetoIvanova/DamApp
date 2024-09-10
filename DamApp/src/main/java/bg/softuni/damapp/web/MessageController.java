package bg.softuni.damapp.web;

import bg.softuni.damapp.exception.UnauthorizedException;
import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.dto.MessageDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.ConversationService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final ConversationService conversationService;
    private final MessageSource messageSource;

    public MessageController(MessageService messageService, UserService userService, AdvertisementService advertisementService, ConversationService conversationService, MessageSource messageSource) {
        this.messageService = messageService;
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.conversationService = conversationService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showConversations(Model model, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedException {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        List<ConversationDTO> conversations = conversationService.getActiveConversationsForUser(userByEmail.getId());
        model.addAttribute("conversations", conversations);
        model.addAttribute("unreadCount", messageService.getUnreadMessageCount(userByEmail.getId()));
        return "messages";
    }

    @GetMapping("/conversation/{conversationId}")
    public String showConversation(@PathVariable UUID conversationId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        messageService.markMessagesAsRead(conversationId, userByEmail.getId());
        ConversationDTO conversationDTO = conversationService.getConversationById(conversationId);

        if (conversationDTO == null) {
            return "redirect:/messages";
        }

        List<MessageDTO> messageDTOs = messageService.getMessagesForUser(conversationId);
        model.addAttribute("conversation", conversationDTO);
        model.addAttribute("messages", messageDTOs);
        return "conversation";
    }

    @GetMapping("/conversation/delete/{id}")
    public String deleteConversation(@PathVariable UUID id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            conversationService.deleteConversation(id);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("conversation_delete_success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("conversation_delete_error", null, locale));
        }
        return "redirect:/messages";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam UUID advertisementId, @RequestParam String content, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedException {
        UserDTO senderByEmail = userService.findByEmail(userDetails.getUsername());
        Optional<User> sender = userService.findById(senderByEmail.getId());
        AdDetailsDTO advertisement = advertisementService.getAdDetails(advertisementId);
        Optional<User> recipient = userService.findById(advertisement.ownerId());

        if (sender.isPresent() && recipient.isPresent()) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSender(sender.get());
            messageDTO.setRecipient(recipient.get());
            messageDTO.setAdvertisementId(advertisement.id());
            messageDTO.setContent(content);

            UUID conversationID = messageService.getOrCreateConversationId(
                    sender.get().getId(),
                    recipient.get().getId(),
                    advertisement.id());
            messageDTO.setConversationId(conversationID);

            messageDTO.setCreatedDate(LocalDateTime.now());
            messageDTO.setRead(false);
            messageService.sendMessage(messageDTO);

            return "redirect:/messages";
        }

        return "redirect:/error";
    }

    @PostMapping("/reply")
    public String replyToMessage(@RequestParam UUID conversationId,
                                 @RequestParam String replyContent,
                                 RedirectAttributes redirectAttributes) {
        try {
            messageService.replyToMessage(conversationId, replyContent);
            redirectAttributes.addFlashAttribute("message", "Съобщението беше успешно изпратено.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при изпращането на съобщението.");
        }
        return "redirect:/messages";
    }

    @GetMapping("/unread-count")
    @ResponseBody
    public int getUnreadMessageCount(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        int unreadMessageCount = messageService.getUnreadMessageCount(userByEmail.getId());

        logger.info("Retrieved unread message count for user {}: {}", userDetails.getUsername(), unreadMessageCount);
        return unreadMessageCount;
    }

    @PostMapping("/conversation/mark-as-read")
    @ResponseBody
    @PreAuthorize("permitAll()")
    public String markMessagesAsRead(@RequestParam UUID conversationId, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Get into controller method -> markMessagesAsRead");
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        UUID userId = userByEmail.getId();

        try {
            messageService.markMessagesAsRead(conversationId, userId);
            return "";
        } catch (Exception e) {
            logger.error("Error marking messages as read", e);
            return "Error marking messages as read";
        }
    }

    @GetMapping("/user/{userId}")
    public List<MessageDTO> getMessagesForUser(@PathVariable UUID userId) {
        return messageService.getMessagesForUser(userId);
    }

}
