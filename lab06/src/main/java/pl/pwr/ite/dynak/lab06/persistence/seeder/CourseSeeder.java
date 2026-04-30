package pl.pwr.ite.dynak.lab06.persistence.seeder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CourseRepository;

@Component
public class CourseSeeder {
    private final CourseRepository repository;

    public CourseSeeder(CourseRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            Course breakfastCourse = new Course();
            Course lunchCourse = new Course();
            Course dinnerCourse = new Course();

            breakfastCourse.setMealType(MealTypes.BREAKFAST);
            breakfastCourse.setPrice(11.0);
            lunchCourse.setMealType(MealTypes.LUNCH);
            lunchCourse.setPrice(15.0);
            dinnerCourse.setMealType(MealTypes.DINNER);
            dinnerCourse.setPrice(20.0);

            repository.save(breakfastCourse);
            repository.save(lunchCourse);
            repository.save(dinnerCourse);
        }
    }
}
