package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Payment {
    @Id
    private Long id;
    private boolean isPaid;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Double amount;
    private int paymentDate;
}
