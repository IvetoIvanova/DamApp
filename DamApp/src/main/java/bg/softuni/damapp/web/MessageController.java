package bg.softuni.damapp.web;

import bg.softuni.damapp.model.dto.AdDetailsDTO;
import bg.softuni.damapp.model.dto.ConversationDTO;
import bg.softuni.damapp.model.dto.MessageDTO;
import bg.softuni.damapp.model.dto.UserDTO;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.service.AdvertisementService;
import bg.softuni.damapp.service.MessageService;
import bg.softuni.damapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final AdvertisementService advertisementService;

//    private final NotificationService notificationService;

    public MessageController(MessageService messageService, UserService userService, AdvertisementService advertisementService) {
        this.messageService = messageService;
        this.userService = userService;
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public String showMessages(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        List<ConversationDTO> conversations = messageService.getConversations(userByEmail.getId());
        model.addAttribute("conversations", conversations);
        model.addAttribute("unreadCount", messageService.getUnreadMessageCount(userByEmail.getId()));
        return "messages";
    }

    @GetMapping("/conversation/{conversationId}")
    public String showConversation(@PathVariable UUID conversationId, Model model) {
        List<MessageDTO> messages = messageService.getMessagesForUser(conversationId);
        model.addAttribute("messages", messages);
        return "conversation";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam UUID advertisementId, @RequestParam String content, @AuthenticationPrincipal UserDetails userDetails) {
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

//            notificationService.notifyUser(recipient.get().getId(), "New message received");

            return "redirect:/messages";
        }

        return "redirect:/error";
    }

    @GetMapping("/unread-count")
    @ResponseBody
    public int getUnreadMessageCount(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userByEmail = userService.findByEmail(userDetails.getUsername());
        return messageService.getUnreadMessageCount(userByEmail.getId());
    }

    @PostMapping("/mark-as-read")
    public void markAsRead(@RequestParam UUID conversationId, @RequestParam UUID userId) {
        messageService.markMessagesAsRead(conversationId, userId);
    }

    @GetMapping("/user/{userId}")
    public List<MessageDTO> getMessagesForUser(@PathVariable UUID userId) {
        return messageService.getMessagesForUser(userId);
    }

}
