package com.example.xueleme.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.xueleme.Adapter.ZoneAdapter;
import com.example.xueleme.Lists;
import com.example.xueleme.R;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.models.locals.Zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ForumFragment extends Fragment {

    private ForumViewModel forumViewModel;

    private String[] data = {"数学", "英语"};
    public static final int UPDATE_TEXT = 1;
    private List<Zone> zoneList = new ArrayList<>();
    private ITopicController topicController = new TopicController();
    private ListView listView2;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    zoneList.addAll((List<Zone>)msg.obj);
//                    System.out.println(zoneList.get(0).zoneName);
                    ZoneAdapter zoneAdapter = new ZoneAdapter(getActivity(), R.layout.zone_item, zoneList);
                    listView2.setAdapter(zoneAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        forumViewModel =
                ViewModelProviders.of(this).get(ForumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_forum, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        ListView listView = root.findViewById(R.id.listView);
        listView2 = root.findViewById(R.id.listview2);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Lists.class);
                startActivity(intent);
            }
        });
        topicController.getAllZones(new ActionResultHandler<List<Zone>, String>() {
            @Override
            public void onSuccess(List<Zone> zones) {
                Message message = new Message();
                message.what = UPDATE_TEXT;
                message.obj = zones;
                handler.sendMessage(message);
            }

            @Override
            public void onError(String s) {
                Looper.prepare();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return root;
    }
}