package pl.pwr.ite.dynak.lab06.core.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

@Data
@AllArgsConstructor
public class UpdateCourseRequest {
    private Long courseId;
    private MealTypes mealType;
    private Double price;
}
