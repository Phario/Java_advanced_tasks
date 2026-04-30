package pl.pwr.ite.dynak.lab06.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ActivityLog {
    @Id
    @GeneratedValue
    private Long id;
    private List<String> actions;
}
