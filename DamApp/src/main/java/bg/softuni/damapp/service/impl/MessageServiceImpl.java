package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.repository.MessageRepository;
import bg.softuni.damapp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void cleanUpOldMessages() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(180);
        int deletedMessagesCount = messageRepository.deleteByCreatedDateBefore(thirtyDaysAgo);
        LOGGER.info("Deleted " + deletedMessagesCount + " messages older than 180 days.");
    }
}
