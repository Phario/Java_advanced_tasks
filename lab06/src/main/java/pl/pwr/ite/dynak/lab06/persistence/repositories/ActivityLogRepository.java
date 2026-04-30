package pl.pwr.ite.dynak.lab06.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.ite.dynak.lab06.models.ActivityLog;

@Repository
public interface ActivityLogRepository extends CrudRepository<ActivityLog, Long> {

}
