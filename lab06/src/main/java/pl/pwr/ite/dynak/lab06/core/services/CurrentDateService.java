package pl.pwr.ite.dynak.lab06.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.enums.Actions;
import pl.pwr.ite.dynak.lab06.persistence.models.CurrentDate;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CurrentDateRepository;

@Slf4j
@Service
public class CurrentDateService {

    private final CurrentDateRepository currentDateRepository;
    private final OrderService orderService;
    private final ActionLogService actionLogService;

    public CurrentDateService(CurrentDateRepository currentDateRepository, OrderService orderService, ActionLogService actionLogService) {
        this.currentDateRepository = currentDateRepository;
        this.orderService = orderService;
        this.actionLogService = actionLogService;
    }

    public void skipDays(int daysToSkip) {

        for (int i = 0; i < daysToSkip; i++) {
            advanceOneDay();
        }

        String infoString = String.format("Skipped %d days", daysToSkip);
        log.info(infoString);
        actionLogService.log(infoString, Actions.DAYS_SKIPPED);
    }

    private void advanceOneDay() {
        CurrentDate currentDate = currentDateRepository.findById(1L).orElseThrow();

        int date = currentDate.getDate();

        orderService.processOrders(date);
        orderService.sendReminders(date);

        currentDate.setDate(date + 1);
        currentDateRepository.save(currentDate);
    }


    public Integer getCurrentDate() {
        return currentDateRepository.findById(1L).orElseThrow().getDate();
    }
}
