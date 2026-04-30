package pl.pwr.ite.dynak.lab06.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealTypes mealType;

    private Double price;
}
