package pl.pwr.ite.dynak.beans;


import pl.pwr.ite.dynak.utils.Ticket;

import java.util.Set;

public interface BoothServiceMXBean {
    void serveTicket();
    int getId();
    String getName();
    int getCurrentTicket();
    Set<String> getSupportedCategories();
    void addSupportedCategory(String category);
    void removeSupportedCategory(String category);
}
