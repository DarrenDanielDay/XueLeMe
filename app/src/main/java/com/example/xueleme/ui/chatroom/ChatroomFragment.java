package com.example.xueleme.ui.chatroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.example.xueleme.LoginActivity;
import com.example.xueleme.R;
import com.example.xueleme.jc_group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import FunctionPackge.GroupChat;
import FunctionPackge.Groupkey;

public class ChatroomFragment extends Fragment {
    private ChatroomViewModel chatroomViewModel;

    public List<Groupkey>g_list= new ArrayList<>();
    // private String[] groupdata ={"math","PE"};


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chatroomViewModel =
                ViewModelProviders.of(this).get(ChatroomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView mygroup_list =(ListView) root.findViewById(R.id.listview2);
        final List <String>lists= new ArrayList<>();
        g_list=chatroomViewModel.g_list;
        for(int i = 0; i<= LoginActivity.users.chatGroupMap.size(); i++){
            lists.add(g_list.get(i).groupName);
        }

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lists);
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
                String groupname = lists.get(position);
                Intent intent1 =new Intent(getActivity(), GroupChat.class);
                intent1.putExtra("groupname",groupname);
                startActivity(intent1);
            }



        });



        return root;

    }
}