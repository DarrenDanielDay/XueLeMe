package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.NotificationDetail;

public class Notification {
    public Integer id;
    public NotificationTypeEnum notificationType;
    public String content;
    public static Notification fromDetail(NotificationDetail detail) {
        Notification notification = new Notification();
        notification.id = detail.id;
        notification.content = detail.content;
        notification.notificationType = NotificationTypeEnum.values()[detail.notificationTypeEnum];
        return notification;
    }
}
