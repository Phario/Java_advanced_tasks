package pl.pwr.ite.dynak;

import pl.pwr.ite.dynak.beans.BoothServiceMXBean;
import pl.pwr.ite.dynak.services.NotificationService;
import pl.pwr.ite.dynak.services.TicketingService;
import pl.pwr.ite.dynak.utils.Ticket;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.HashSet;
import java.util.Set;

public class Booth extends NotificationBroadcasterSupport implements BoothServiceMXBean {
    private int id;
    private String name;
    private Set<String> supportedCategories;
    private int currentTicket;
    private final TicketingService ticketingService;
    private final NotificationService notificationService;
    private int sequenceNumber = 0;

    public Booth(int id, String name, TicketingService ticketingService, NotificationService notificationService) {
        this.id = id;
        this.name = name;
        this.supportedCategories = new HashSet<>();
        this.ticketingService = ticketingService;
        this.notificationService = notificationService;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getId() {
        return id;
    }
    public int getCurrentTicket() {
        return currentTicket;
    }

    @Override
    public Set<String> getSupportedCategories() {
        return supportedCategories;
    }

    @Override
    public void addSupportedCategory(String category) {
        supportedCategories.add(category);

        Notification notification = notificationService.getNotification(
                "Booth #" + id + " [" + name + "] added category via UI: " + category,
                this,
                sequenceNumber++
        );
        sendNotification(notification);
    }

    @Override
    public void removeSupportedCategory(String category) {
        supportedCategories.remove(category);
        Notification notification = notificationService.getNotification(
                "Booth #" + id + " [" + name + "] removed category via UI: " + category,
                this,
                sequenceNumber++
        );
        sendNotification(notification);
    }

    @Override
    public void serveTicket() {
        if (ticketingService.getQueue().isEmpty() || supportedCategories.isEmpty()) {
            return;
        }

        Ticket ticketToServe = ticketingService.getTicket(supportedCategories);

        if (ticketToServe != null) {
            currentTicket = ticketToServe.getTicketId();
            ticketingService.getQueue().remove(ticketToServe);
        }
    }
}
