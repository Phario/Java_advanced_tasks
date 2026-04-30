package pl.pwr.ite.dynak.lab06.persistence.seeder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.pwr.ite.dynak.lab06.persistence.models.Client;
import pl.pwr.ite.dynak.lab06.persistence.repositories.ClientRepository;

@Component
public class UserSeeder {
    private final ClientRepository repository;

    public UserSeeder(ClientRepository repository, ClientRepository repository1) {
        this.repository = repository1;
    }

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            Client admin = new Client();
            Client client1 = new Client();
            Client client2 = new Client();
            Client client3 = new Client();

            admin.setName("admin");
            client1.setName("clientOne");
            client2.setName("clientTwo");
            client3.setName("clientThree");

            repository.save(admin);
            repository.save(client1);
            repository.save(client2);
            repository.save(client3);
        }
    }
}
