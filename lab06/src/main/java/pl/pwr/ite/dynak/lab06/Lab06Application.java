package pl.pwr.ite.dynak.lab06;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.pwr.ite.dynak.lab06.core.services.SimulationService;

@SpringBootApplication
public class Lab06Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab06Application.class, args);
    }
}
