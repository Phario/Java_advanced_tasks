package pl.pwr.ite.dynak.lab06.core.services;

import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.enums.Actions;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ActionLogService actionLogService;

    public CourseService(CourseRepository courseRepository, ActionLogService actionLogService) {
        this.courseRepository = courseRepository;
        this.actionLogService = actionLogService;
    }

    public void updateCourse(Course course) {
        if (courseRepository.existsById(course.getId())) {
            courseRepository.save(course);
            actionLogService.log("Course " + course.getId() + "was updated", Actions.COURSE_UPDATED);
        } else {
            throw new IllegalArgumentException("Course with ID " + course.getId() + " does not exist");
        }
    }
}
