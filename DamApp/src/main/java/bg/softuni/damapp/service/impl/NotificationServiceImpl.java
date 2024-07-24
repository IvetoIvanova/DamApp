//package bg.softuni.damapp.service.impl;
//
//import bg.softuni.damapp.service.NotificationService;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//@Service
//public class NotificationServiceImpl implements NotificationService {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public void notifyUser(UUID userId, String message) {
//        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", message);
//    }
//}
