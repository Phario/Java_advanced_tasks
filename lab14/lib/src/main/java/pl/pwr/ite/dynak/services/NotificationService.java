package pl.pwr.ite.dynak.services;

import javax.management.Notification;

public class NotificationService {
    public Notification getNotification(String message, Object source, long sequenceNumber) {
        return new Notification(
                "standpoint.config.changed",
                source,
                sequenceNumber,
                System.currentTimeMillis(),
                message
        );
    }

}
