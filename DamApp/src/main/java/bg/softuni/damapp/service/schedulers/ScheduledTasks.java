package bg.softuni.damapp.service.schedulers;

import bg.softuni.damapp.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private final ReportingService reportingService;

    public ScheduledTasks(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @Scheduled(cron = "0 0 9,23 * * *")
    public void performTaskTwiceADay() {
        LOGGER.info("Scheduled task executed at " + LocalDateTime.now());
        reportingService.reportUserRegistrations();
    }
}
