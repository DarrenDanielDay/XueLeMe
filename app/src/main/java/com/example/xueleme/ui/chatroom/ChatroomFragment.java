package com.example.xueleme.ui.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.LoginActivity;
import com.example.xueleme.R;
import com.example.xueleme.jc_group;
import com.example.xueleme.models.locals.ChatGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.xueleme.business.IChatGroupController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.xueleme.GroupChat;
import FunctionPackge.Groupkey;
import interface_packge.RequestHandler;

public class ChatroomFragment extends Fragment {
    private ChatroomViewModel chatroomViewModel;

    public static List<Groupkey>g_list= new ArrayList<>();
    private List<ChatGroup>my_list =new ArrayList<>();
    private ChatGroupController chatGroupController;
    private ArrayAdapter<String>adapter;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chatroomViewModel =
                ViewModelProviders.of(this).get(ChatroomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView mygroup_list =(ListView) root.findViewById(R.id.listview2);
        final List <String>lists= new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lists);
        // g_list=chatroomViewModel.g_list;

                LoginActivity.users.MyJoinedGroup(new RequestHandler() {  //查询加入的组
                    @Override
                    public void requestSuccess() {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "获得成功", Toast.LENGTH_LONG).show();
                        Map map = LoginActivity.users.getChatGroupMap();
                        Log.d("sfsfs", "sfsfsfs" + String.valueOf(map.size()));
                        Iterator<Map.Entry<Groupkey, Integer>> iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<Groupkey, Integer> entry = iter.next();
                            Groupkey key = entry.getKey();
                            Integer value = entry.getValue();
                            Log.d("sfsfsfsf", "加入的群：" + key.groupName + "$$$$$$" + key.groupId + "&&&&&&" + value);

                        }
                        Looper.loop();
                    }

                    @Override
                    public void requestFailed() {
                        Looper.prepare();
                        Toast.makeText(getActivity(),"获得失败",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
                LoginActivity.users.MyCreateGroup(new RequestHandler() {    //查询创建的群
                    @Override
                    public void requestSuccess() {
                        Looper.prepare();
                        Toast.makeText(getActivity(),"获得成功",Toast.LENGTH_LONG).show();
                        Map map=LoginActivity.users.getChatGroupMap();
                        Iterator<Map.Entry<Groupkey,Integer>> iter = map.entrySet().iterator();
                        while(iter.hasNext()){
                            Map.Entry<Groupkey,Integer> entry = iter.next();
                            Groupkey key = entry.getKey();
                            Integer value = entry.getValue();
                            Log.d("sfsfsfsf","创建的群："+key.groupName+"$$$$$$"+key.groupId+"&&&&&&"+value);

                        }
                        Looper.loop();
                    }

                    @Override
                    public void requestFailed() {
                        Looper.prepare();
                        Toast.makeText(getActivity(),"获得失败",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });


        Set<Groupkey> keySet = LoginActivity.users.chatGroupMap.keySet();
        g_list.addAll(keySet);
        for(int i = 0; i< LoginActivity.users.chatGroupMap.size(); i++){
            lists.add(g_list.get(i).groupName);
        }


        mygroup_list.setAdapter(adapter);

        FloatingActionButton btn_group = root.findViewById(R.id.floatingActionButton2);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), jc_group.class);
                intent.putExtra("extra", getActivity().getIntent().getStringExtra("extra_data"));
                startActivity(intent);
            }
        });

        mygroup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 =new Intent(getActivity(), GroupChat.class);

                intent1.putExtra("position",position+"");
                startActivity(intent1);
            }


        });


        return root;
    }

}