package com.example.xueleme.business;

import android.util.Log;

import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.NotificationTypeEnum;
import com.example.xueleme.models.responses.ChatMessageDetail;
import com.example.xueleme.models.responses.NotificationDetail;
import com.example.xueleme.utils.FormatHelper;
import com.example.xueleme.utils.HttpRequester;
import com.google.gson.Gson;
import com.microsoft.signalr.Action2;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NotificationHub {
    public final String PORT = ":5000";
    public final String PATH = "api/NotificationHub";
    public final HubConnection hubConnection;
    public final Publisher<ChatMessage> chatMessagePublisher;
    public final Publisher<Notification> notificationPublisher;
    public final Publisher<Throwable> connectionClosedPublisher;
    private boolean isConnected = false;

    public synchronized void setConnected(boolean connected) {
        isConnected = connected;
    }

    private boolean isJoined = false;
    private static NotificationHub hub = null;

    public static synchronized NotificationHub getInstance() {
        if (hub == null) {
            hub = new NotificationHub();
        }
        return hub;
    }

    private NotificationHub() {
        chatMessagePublisher = new Publisher<>();
        notificationPublisher = new Publisher<>();
        connectionClosedPublisher = new Publisher<>();
        hubConnection = HubConnectionBuilder.create(HttpRequester.HOST + PORT + HttpRequester.BASE_PATH + PATH).build();
        hubConnection.on("OnNotify", new Action2<Integer, String>() {
            @Override
            public void invoke(Integer param1, String param2) {
                Log.d("OnNotify Invoke", param2);
                NotificationTypeEnum notificationTypeEnum = NotificationTypeEnum.values()[param1];
                Notification notification = new Notification();
                notification.notificationType = notificationTypeEnum;
                notification.content = param2;

                if (notificationTypeEnum == NotificationTypeEnum.CHAT_MESSAGE) {
                    Gson gson = new Gson();
                    Map<String, Object> json = gson.fromJson(param2, Map.class);
                    ChatMessageDetail detail = new ChatMessageDetail().parse(json);
                    ChatMessage chatMessage = ChatMessage.fromDetail(detail);
                    chatMessagePublisher.publish(chatMessage);
                } else {
                    notificationPublisher.publish(notification);
                }
            }
        }, Integer.class, String.class);
        hubConnection.onClosed(new OnClosedCallback() {
            @Override
            public void invoke(Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                }
                Log.d("lost hub connection", "与服务器Hub丢失连接：" + (exception == null ? "未知原因" : exception.getMessage()));
                setConnected(false);
                setJoined(false);
                connectionClosedPublisher.publish(exception);
            }
        });
    }

    public void connect(ActionResultHandler<String, Throwable> handler) {
        if (isConnected()) {
            Log.d("already connected", "已连接，请勿重复连接");
            handler.onSuccess("Hub已经连接");
            return;
        }
        Log.d("connecting", "正在与服务器Hub构建连接");
        Completable completable = hubConnection.start().doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                setConnected(true);
                Log.d("Hub connect", "成功与服务器Hub构建连接");
                handler.onSuccess("Hub连接成功");
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("Hub connect", "与服务器Hub连接失败：" + throwable.getMessage());
                handler.onError(throwable);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    completable.blockingAwait();
                } catch (Throwable throwable) {
                    Log.e("blocking await", throwable.getMessage());
                }
            }
        }).start();
    }

    public void joinAsUser(int id, ActionResultHandler<String, Throwable> handler) {
        if (isConnected()) {
            if (!isJoined()) {
                Log.d("Hub invoke joinAsUser", "正在加入聊天室");
                hubConnection.invoke(String.class, "JoinAsUser", id).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("Hub invoke joinAsUser", s);
                        setJoined(true);
                        handler.onSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        String message = FormatHelper.exceptionFormat(throwable);
                        Log.d("Hub invoke error", message);
                        handler.onError(throwable);
                    }
                });
            } else {
                handler.onSuccess("已经加入聊天室");
            }
        } else {
            connect(new ActionResultHandler<String, Throwable>() {
                @Override
                public void onSuccess(String o) {
                    joinAsUser(id, handler);
                }

                @Override
                public void onError(Throwable throwable) {
                    handler.onError(throwable);
                }
            });
        }
    }

    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        hubConnection.stop();
        Log.d("Hub disconnect", "与服务器Hub断开连接");
        setConnected(false);
        setJoined(false);
    }

    public synchronized boolean isConnected() {
        return isConnected;
    }

    public synchronized boolean isJoined() {
        return isJoined;
    }

    public synchronized void setJoined(boolean joined) {
        isJoined = joined;
    }
}
