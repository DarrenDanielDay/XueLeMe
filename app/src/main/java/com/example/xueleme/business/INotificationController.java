package com.example.xueleme.business;

import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;

public interface INotificationController {
    void subscribeNotification(Subscriber<Notification> subscriber);
    void getOfflineNotifications(User user);
}
