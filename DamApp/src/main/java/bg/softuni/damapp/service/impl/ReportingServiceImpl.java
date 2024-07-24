package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportingServiceImpl implements ReportingService {
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportingServiceImpl.class);

    public ReportingServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void reportUserRegistrations() {
        long userCount = userRepository.count();
        LOGGER.info("Total registered users as of " + LocalDateTime.now() + ": " + userCount);
    }
}
