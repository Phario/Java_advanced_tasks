package pl.pwr.ite.dynak.lab06.core.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.enums.Actions;
import pl.pwr.ite.dynak.lab06.persistence.models.ActivityLog;
import pl.pwr.ite.dynak.lab06.persistence.repositories.ActivityLogRepository;

@Slf4j
@Service
public class ActionLogService {
    private final ActivityLogRepository activityLogRepository;

    public ActionLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Transactional
    public void log(String message, Actions action) {
        String actionString = "[=== " + action.name() + " ===] " + message;
        ActivityLog actionLog = new ActivityLog();

        actionLog.setAction(actionString);
        log.info(actionString);

        activityLogRepository.save(actionLog);
    }
}
