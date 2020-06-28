package com.example.xueleme.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.xueleme.ChatRoomActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.NotificationHub;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.models.databases.MyEntityDatabaseHelper;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.ChatRecord;
import com.example.xueleme.models.locals.User;

import java.util.function.Consumer;

public class NotificationService extends Service {
    NotificationManager manager = null;
    private int currentNotificationId = 0;
    private int currentRequestCode = 0;

    private int getCurrentNotificationId() {
        return currentNotificationId++;
    }

    public int getCurrentRequestCode() {
        return currentRequestCode++;
    }

    private static final String CHANNEL_ID = "NotificationServiceChannel";
    private static final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "myChannel", NotificationManager.IMPORTANCE_HIGH);

    static {
        channel.setDescription("this is a description");
        channel.enableLights(true);
        channel.setLightColor(Color.DKGRAY);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
    }

    private Subscriber<ChatMessage> chatMessageSubscriber = new Subscriber<>(new Consumer<ChatMessage>() {
        @Override
        public void accept(ChatMessage message) {
            Log.d(NotificationService.class.getSimpleName(), "收到一条聊天消息");
            User user = accountController.getCurrentUser();
            if (user == null || user.id.equals(message.senderId)) {
                return;
            }
            MyEntityDatabaseHelper helper = new MyEntityDatabaseHelper(NotificationService.this);
            helper.chatMessageDatabaseAccessor.insertOne(message, helper.getReadableDatabase());
            Log.d(NotificationService.class.getSimpleName(), "Pending intent groupId = " + message.groupId);
            Intent intent = ChatRoomActivity.startIntent(NotificationService.this, message.groupId, message.groupName);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    NotificationService.this,
                    getCurrentRequestCode(),
                    intent,
                    PendingIntent.FLAG_ONE_SHOT);
            String display = message.type == ChatRecord.MessageType.IMAGE ? "[图片]" : message.content;
            NotificationService.this.showNotification("您有亿条未读消息", display, pendingIntent);
        }
    });
    private Subscriber<com.example.xueleme.models.locals.Notification> notificationSubscriber = new Subscriber<>(new Consumer<com.example.xueleme.models.locals.Notification>() {
        @Override
        public void accept(com.example.xueleme.models.locals.Notification notification) {
            Log.d("NotificationService", "收到一条其他消息");
            NotificationService.this.showNotification("学了么", notification.content);
        }
    });
    private IAccountController accountController;

    public NotificationService() {
        Log.d(this.getClass().getName(), "通知服务正在初始化");
        NotificationHub.getInstance().chatMessagePublisher.attach(this.chatMessageSubscriber);
        NotificationHub.getInstance().notificationPublisher.attach(this.notificationSubscriber);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationService", "onCreate");
        accountController = new AccountController(this);
        accountController.getCurrentUser();
    }

    public void showNotification(String title, String content) {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        manager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_baseline_comment_24)
                .setChannelId(CHANNEL_ID)
                .build();
        manager.notify(getCurrentNotificationId(), notification);
    }

    public void showNotification(String title, String content, PendingIntent pendingIntent) {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        manager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentText(title)
                .setAutoCancel(true)
                .setContentTitle(content)
                .setSmallIcon(R.drawable.ic_baseline_comment_24)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(getCurrentNotificationId(), notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("onBind", "被调用了");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        chatMessageSubscriber.detach();
        super.onDestroy();
    }
}
