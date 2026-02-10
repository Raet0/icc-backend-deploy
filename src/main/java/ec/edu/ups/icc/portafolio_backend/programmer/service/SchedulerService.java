package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AdvisoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SchedulerService {

    private final AdvisoryRepository advisoryRepository;
    private final NotificationService notificationService;

    public SchedulerService(AdvisoryRepository advisoryRepository, NotificationService notificationService) {
        this.advisoryRepository = advisoryRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 300000)
    public void notifyUpcomingAdvisories() {
        LocalDateTime from = LocalDateTime.now().plusMinutes(30);
        LocalDateTime to = LocalDateTime.now().plusHours(24);

        advisoryRepository
            .findByStatusAndReminderSentFalseAndScheduledAtBetween(AdvisoryStatus.APROBADA, from, to)
            .forEach(a -> {
                notificationService.notifyAdvisoryReminder(a);
                a.setReminderSent(true);
                advisoryRepository.save(a);
            });
    }
}