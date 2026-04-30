package pl.pwr.ite.dynak.lab06.persistence.seeder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.pwr.ite.dynak.lab06.persistence.models.CurrentDate;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CurrentDateRepository;

@Component
public class DateSeeder {
    private final CurrentDateRepository repository;

    public DateSeeder(CurrentDateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            CurrentDate date = new CurrentDate();
            date.setId(1L);
            date.setDate(0);
            repository.save(date);
        }
    }
}
