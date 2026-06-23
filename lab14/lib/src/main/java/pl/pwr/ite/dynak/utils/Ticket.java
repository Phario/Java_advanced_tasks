package pl.pwr.ite.dynak.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    String category;
    int ticketId;
    int priority; // the higher the number, the higher the priority
}
