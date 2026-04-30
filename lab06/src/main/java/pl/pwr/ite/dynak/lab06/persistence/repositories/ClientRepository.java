package pl.pwr.ite.dynak.lab06.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.ite.dynak.lab06.models.Client;
import pl.pwr.ite.dynak.lab06.models.Course;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}
