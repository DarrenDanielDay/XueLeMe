package com.example.xueleme.business;

import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.NotificationDetail;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.utils.HttpRequester;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class NotificationController extends RequestController implements INotificationController {
    @Override
    public void subscribeNotification(Subscriber<Notification> subscriber) {
        synchronized (NotificationHub.class) {
            NotificationHub.getInstance().notificationPublisher.attach(subscriber);
        }
    }

    @Override
    public void getOfflineNotifications(UserAction<User, List<Notification>, String> action) {
        handleGetAction(action, "api/Notification/MyNotifications/" + action.data.id, ServiceResultEnum.EXIST, ServiceResult.listParser(NotificationDetail.class), new ResponseModelAdapter<List<NotificationDetail>, List<Notification>>() {
            @Override
            public List<Notification> convert(List<NotificationDetail> notificationDetails) {
                List<Notification> notifications = new ArrayList<>();
                for (NotificationDetail detail: notificationDetails) {
                    notifications.add(Notification.fromDetail(detail));
                }
                return notifications;
            }
        });
    }

    @Override
    public void readNotification(UserAction<Notification, String, String> action) {
        HttpRequester.getInstance().delete("api/Notification/Read/" + action.data.id, null, new ActionResultHandler<ServiceResult<Object>, String>() {
            @Override
            public void onSuccess(ServiceResult<Object> objectServiceResult) {
                action.resultHandler.onSuccess(objectServiceResult.detail);
            }

            @Override
            public void onError(String s) {
                action.resultHandler.onSuccess(s);
            }
        });
    }

    @Override
    public void readNotifications(UserAction<List<Notification>, String, String> action) {
        JSONArray jsonArray = new JSONArray();
        for (Notification notification: action.data) {
            jsonArray.put(notification.id);
        }
        HttpRequester.getInstance().delete("api/Notification/ReadMany", jsonArray.toString(), new ActionResultHandler<ServiceResult<Object>, String>() {
            @Override
            public void onSuccess(ServiceResult<Object> objectServiceResult) {
                action.resultHandler.onSuccess(objectServiceResult.detail);
            }

            @Override
            public void onError(String s) {
                action.resultHandler.onError(s);
            }
        });
    }
}
