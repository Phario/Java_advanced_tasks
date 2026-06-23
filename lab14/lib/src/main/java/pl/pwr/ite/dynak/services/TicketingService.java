package pl.pwr.ite.dynak.services;

import pl.pwr.ite.dynak.Booth;
import pl.pwr.ite.dynak.beans.TicketingServiceMXBean;
import pl.pwr.ite.dynak.utils.Ticket;
import pl.pwr.ite.dynak.utils.TicketComparator;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.*;

public class TicketingService extends NotificationBroadcasterSupport implements TicketingServiceMXBean {
    private Map<String, Integer> priorities = new HashMap<>();
    private List<Booth> booths;
    private long sequenceNumber = 1;
    private PriorityQueue<Ticket> queue = new PriorityQueue<>(new TicketComparator());
    private int ticketCounter = 1;
    private final NotificationService notificationService = new NotificationService();
    @Override
    public Map<String, Integer> getPriorities() {
        return priorities;
    }

    @Override
    public void setPriorities(Map<String, Integer> priorities) {
        this.priorities = priorities;
    }

    public void setPrioritiesFromUI(Map<String, Integer> newPriorities) {
        this.priorities = newPriorities;

        Notification notification = notificationService.getNotification(
                "Parameters changed via app UI. Current categories count: " + newPriorities.size(),
                this,
                sequenceNumber++
        );

        sendNotification(notification);
    }

    @Override
    public Ticket generateTicket(String categoryName) {
        if (!priorities.containsKey(categoryName)) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setCategory(categoryName);
        ticket.setPriority(priorities.get(categoryName));
        ticket.setTicketId(ticketCounter++);

        queue.add(ticket);
        return ticket;
    }

    @Override
    public Ticket getTicket(Set<String> supportedCategories) {
        if (queue.isEmpty() || supportedCategories == null || supportedCategories.isEmpty()) {
            return null;
        }

        var ticketToRemove = queue.stream()
                .sorted(new TicketComparator())
                .filter(ticket -> supportedCategories.contains(ticket.getCategory()))
                .findFirst()
                .orElse(null);

        try {
            return ticketToRemove;
        } finally {
            queue.remove(ticketToRemove);
        }
    }

    public PriorityQueue<Ticket> getQueue() {
        return queue;
    }

    public void setBooths(List<Booth> booths) {
        this.booths = booths;
    }
}
