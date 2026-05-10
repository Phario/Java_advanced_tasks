package pl.pwr.ite.dynak.lab06.core.requests;

import pl.pwr.ite.dynak.lab06.persistence.enums.DietTypes;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

import java.util.Set;

public record CreateOrderRequest(
        Long clientId,
        Set<MealTypes> mealTypes,
        DietTypes dietType,
        int startDate,
        int endDate)
{}
