package pl.pwr.ite.dynak.lab06.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CurrentDate {
    @Id
    private Long id = 1L;
    private int date = 0;
}
