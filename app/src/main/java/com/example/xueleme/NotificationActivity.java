package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.INotificationController;
import com.example.xueleme.business.NotificationController;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.NotificationTypeEnum;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ChatMessageDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    IAccountController iAccountController=new AccountController(NotificationActivity.this);
    INotificationController iNotificationController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ListView listView=findViewById(R.id.line3);
        iNotificationController=new NotificationController();
        User user=iAccountController.getCurrentUser();
        iNotificationController.getOfflineNotifications(new UserAction<>(user, new ActionResultHandler<List<Notification>, String>() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                List list=new ArrayList();
                for(Notification h:notifications)
                {
                    Map map=new HashMap<String,Object>();
                    if(h.notificationType!=NotificationTypeEnum.CHAT_MESSAGE)
                    {
                        map.put("type",h.notificationType);
                        map.put("id",h.id);
                        map.put("content",h.content);
                    }
                    else
                    {
                        map.put("type",h.notificationType);
                        map.put("id",h.id);
                        Gson gson= new Gson();
                        Map mapp=new HashMap<String, Object>();
                        mapp=gson.fromJson(h.content,map.getClass());
                        ChatMessageDetail chatMessageDetail=new ChatMessageDetail();
                        chatMessageDetail=chatMessageDetail.parse(mapp);
                        String message=chatMessageDetail.createdTime+"\n"+chatMessageDetail.group+"  "+"消息发送者ID:"+chatMessageDetail.id+"\n"+chatMessageDetail.content;
                        map.put("content",message);
                    }
                    list.add(map);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(NotificationActivity.this, list, R.layout.item_notifacation, new String[]{"type","id", "content"}, new int[]{R.id.id1, R.id.id2,R.id.id3});
                NotificationActivity.this.runOnUiThread(()->listView.setAdapter(simpleAdapter));
            }
            @Override
            public void onError(String s) {
                Looper.prepare();
                Toast.makeText(NotificationActivity.this, "获取离线通知失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }));
    }
}