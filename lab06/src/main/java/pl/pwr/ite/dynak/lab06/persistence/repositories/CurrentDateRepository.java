package pl.pwr.ite.dynak.lab06.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pwr.ite.dynak.lab06.persistence.models.CurrentDate;

public interface CurrentDateRepository extends CrudRepository<CurrentDate, Long> {
}
