package pl.pwr.ite.dynak.lab06.persistence.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.lab06.persistence.enums.DietTypes;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ElementCollection(targetClass = MealTypes.class)
    @Enumerated(EnumType.STRING)
    private Set<MealTypes> mealTypes;

    @Enumerated(EnumType.STRING)
    private DietTypes dietType;
    private double price;
    private boolean isPaid;
    private int paymentDate;
    private int startDate;
    private int endDate;
}
