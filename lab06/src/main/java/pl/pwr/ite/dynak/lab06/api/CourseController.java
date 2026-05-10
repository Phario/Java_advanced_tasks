package pl.pwr.ite.dynak.lab06.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.ite.dynak.lab06.core.requests.CreateOrderRequest;
import pl.pwr.ite.dynak.lab06.core.requests.UpdateCourseRequest;
import pl.pwr.ite.dynak.lab06.core.services.CourseService;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;

import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateCourse(@RequestBody UpdateCourseRequest request) {
        Course course = new Course();

        course.setId(request.getCourseId());
        course.setMealType(request.getMealType());
        course.setPrice(request.getPrice());

        courseService.updateCourse(course);
        return ResponseEntity.ok().build();
    }
}
