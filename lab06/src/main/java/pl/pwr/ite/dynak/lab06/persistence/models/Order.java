package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private boolean isPaid;
    private int paymentDate;
    private int startDate;
    private int endDate;
}
