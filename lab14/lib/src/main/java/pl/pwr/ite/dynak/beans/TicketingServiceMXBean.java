package pl.pwr.ite.dynak.beans;

import pl.pwr.ite.dynak.utils.Ticket;

import java.util.Map;
import java.util.Set;

public interface TicketingServiceMXBean {
    Map<String, Integer> getPriorities();
    Ticket getTicket(Set<String> categories);
    Ticket generateTicket(String categoryName);
    void setPriorities(Map<String, Integer> priorities);
}
