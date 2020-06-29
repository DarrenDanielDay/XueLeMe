package com.example.xueleme.services;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.xueleme.BaseActivity;
import com.example.xueleme.ChatRoomActivity;
import com.example.xueleme.HandleJoinRequestActivity;
import com.example.xueleme.LoginActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.DefaultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.NotificationHub;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.databases.MyEntityDatabaseHelper;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.ChatRecord;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.UserAndGroupDetail;
import com.example.xueleme.models.responses.UserDetail;
import com.google.gson.Gson;

import java.util.Map;
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

    private Subscriber<ChatMessage> chatMessageSubscriber;
    private Subscriber<com.example.xueleme.models.locals.Notification> notificationSubscriber;
    private IAccountController accountController;

    public NotificationService() {
        Log.d(this.getClass().getName(), "通知服务正在初始化");
        chatMessageSubscriber = new Subscriber<>(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage message) {
                Log.d(NotificationService.class.getSimpleName(), "收到一条聊天消息");
                User user = accountController.getCurrentUser();
                MyEntityDatabaseHelper helper = new MyEntityDatabaseHelper(NotificationService.this);
                helper.chatMessageDatabaseAccessor.insertOne(message, helper.getReadableDatabase());
                Log.d(NotificationService.class.getSimpleName(), "Pending intent groupId = " + message.groupId);
                if (user.id.equals(message.senderId)) {
                    return;
                }
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
        notificationSubscriber = new Subscriber<>(new Consumer<com.example.xueleme.models.locals.Notification>() {
            @Override
            public void accept(com.example.xueleme.models.locals.Notification notification) {
                Log.d("NotificationService", "收到一条其他消息");
                Log.d("NotificationService", "消息类型：" + notification.notificationType);
                Intent intent = null;
                NotificationService that = NotificationService.this;
                switch (notification.notificationType) {
                    case REPLIED:
                    case MENTIONED:
                        Log.d(that.getClass().getSimpleName(), "尚未实现");
                        // TODO
                        return;
                    case JOIN_REQUEST:
                        UserAndGroupDetail detail = new UserAndGroupDetail().parse(new Gson().fromJson(notification.content, Map.class));
                        intent = HandleJoinRequestActivity.startIntent(that, detail.user.nickname, detail.user.avatar, detail.group.name, detail.user.id, detail.group.id, detail.requestId);
                        PendingIntent pendingIntent = PendingIntent.getActivity(that, getCurrentRequestCode(), intent, PendingIntent.FLAG_ONE_SHOT);
                        NotificationService.this.showNotification("学了么",
                                "您有一条待处理的加群申请",
                                pendingIntent);
                        break;
                    case USER_KICKED:
                        detail = new UserAndGroupDetail().parse(new Gson().fromJson(notification.content, Map.class));
                        String userDisplay = detail.user.id.equals(accountController.getCurrentUser().id) ? "您" : detail.user.nickname;
                        if (detail.group.owner.id.equals(accountController.getCurrentUser().id)) {
                            // 不通知群主，踢人操作者只能是群主
                            return;
                        }
                        showNotification("学了么", String.format("%s 已被移出群 %s(%d)", userDisplay, detail.group.name, detail.group.id));
                        break;
                    case USER_QUIT:
                    case NEW_MEMBER_JOINED:
                        showNotification("学了么", notification.content);
                        break;
                    case FORCED_OUT:
                        Log.d("Forced out", "looper之前");
                        Looper.prepare();
                        Intent intentForceOut = new Intent(BaseActivity.SIGNAL_FORCE_OUT);
                        BaseActivity baseActivity;
                        synchronized (BaseActivity.class) {
                            if (BaseActivity.activityStack.isEmpty()) {
                                baseActivity = BaseActivity.activities.iterator().next();
                            } else {
                                baseActivity = BaseActivity.activityStack.peek();
                            }
                        }
                        AlertDialog.Builder dialog = new AlertDialog.Builder(baseActivity);
                        dialog.setTitle("下线通知");
                        dialog.setMessage(notification.content);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accountController.logout(new UserAction<>(0, new DefaultHandler<>()));
                                sendBroadcast(intentForceOut);
                                startActivity(new Intent(that, LoginActivity.class));
                            }
                        });
                        dialog.show();
                        Looper.loop();
                        Log.d("Forced out", "looper之后");
                        break;
                    default:
                        break;
                }
            }
        });
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
        notificationSubscriber.detach();
        super.onDestroy();
    }
}
