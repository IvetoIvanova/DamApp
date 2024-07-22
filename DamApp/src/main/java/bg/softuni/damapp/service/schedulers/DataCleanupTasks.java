package bg.softuni.damapp.service.schedulers;

import bg.softuni.damapp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataCleanupTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataCleanupTasks.class);
    private final MessageService messageService;

    public DataCleanupTasks(MessageService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpOldMessages() {
        LOGGER.info("Scheduled task executed at " + LocalDateTime.now());
        messageService.cleanUpOldMessages();
    }
}
