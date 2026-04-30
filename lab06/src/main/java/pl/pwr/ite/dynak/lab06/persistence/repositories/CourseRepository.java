package pl.pwr.ite.dynak.lab06.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;

import java.util.List;


@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
}
