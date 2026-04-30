package pl.pwr.ite.dynak.lab06.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.ite.dynak.lab06.persistence.models.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByName(String name);
}
