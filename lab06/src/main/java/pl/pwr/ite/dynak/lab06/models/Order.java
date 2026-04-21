package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Double price;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToOne
    private Payment payment;
    private int startDate;
    private int endDate;
}
