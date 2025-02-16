package com.example.exchange;

public class UserNotificationClass {
    private int notificationId;
    private String message;
    private String notifDate;

    public UserNotificationClass(int notificationId, String message, String notifDate) {
        this.notificationId = notificationId;
        this.message = message;
        this.notifDate = notifDate;
    }

    public int getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public String getNotifDate() { return notifDate; }
}
