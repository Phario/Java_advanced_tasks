package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany()
    @JoinColumn(name = "client_id")
    private List<Order> orders;
}
