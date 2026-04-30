package pl.pwr.ite.dynak.lab06.persistence.models;

import jakarta.persistence.*;
import pl.pwr.ite.dynak.lab06.utils.CourseTypes;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private CourseTypes type;
    private Double price;
}
