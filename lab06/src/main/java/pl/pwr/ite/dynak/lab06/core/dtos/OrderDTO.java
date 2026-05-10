package pl.pwr.ite.dynak.lab06.core.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.ite.dynak.lab06.persistence.enums.DietTypes;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

import java.util.Set;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long clientId;
    private Set<MealTypes> mealTypes;
    private DietTypes dietType;
    private double price;
    private boolean isPaid;
    private int paymentDate;
    private int startDate;
    private int endDate;
}
