package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pl.pwr.ite.dynak.lab06.utils.CourseTypes;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    private Long id;
    private String name;
    private CourseTypes type;
    private Double price;
}
