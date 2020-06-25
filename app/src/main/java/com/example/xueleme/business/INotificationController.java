package com.example.xueleme.business;

import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;

import java.util.List;

public interface INotificationController {
    void subscribeNotification(Subscriber<Notification> subscriber);
    void getOfflineNotifications(UserAction<User, List<Notification>, String> action);
    void readNotification(UserAction<Notification, String, String> action);
    void readNotifications(UserAction<List<Notification>, String, String> action);
}
