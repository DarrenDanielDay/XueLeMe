package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.xueleme.Adapter.TopicAdapter;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.locals.Topic;
import com.example.xueleme.models.locals.Zone;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {
//    private String[] data = {"数学", "英语"};
    public static final int UPDATE_TEXT = 1;
    private ITopicController topicController = new TopicController();
    private List<Topic> topicList = new ArrayList<>();
    private ListView listView;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    topicList.addAll((List<Topic>)msg.obj);
//                    System.out.println(zoneList.get(0).zoneName);
                    TopicAdapter topicAdapter = new TopicAdapter(TopicActivity.this, R.layout.topic_item, topicList);
                    listView.setAdapter(topicAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Intent intent = getIntent();
        listView = findViewById(R.id.topic_lv);
        int data = intent.getIntExtra("extra_data", -1);
        Zone zone = new Zone();
        System.out.println(data);
        zone.id = data;
        topicController.getAllTopicsOfZone(new UserAction<>(zone, new ActionResultHandler<List<Topic>, String>() {
            @Override
            public void onSuccess(List<Topic> topics) {
                Message message = new Message();
                message.what = UPDATE_TEXT;
                message.obj = topics;
                handler.sendMessage(message);
            }

            @Override
            public void onError(String s) {

            }
        }));
    }
}