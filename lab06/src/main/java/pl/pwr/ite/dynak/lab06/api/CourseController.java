package pl.pwr.ite.dynak.lab06.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pwr.ite.dynak.lab06.core.services.CourseService;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {


        return ResponseEntity.ok(courseService.getCourses());
    }
}
