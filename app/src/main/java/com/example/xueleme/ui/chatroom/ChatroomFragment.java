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

import com.example.xueleme.MainActivity;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.LoginActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.jc_group;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.User;
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
    private IChatGroupController chatGroupController = new ChatGroupController();
    private ArrayAdapter<String>adapter;
    private List <String>lists= new ArrayList<>();
    private IAccountController iAccountController;
    private User user;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        iAccountController =new AccountController(getActivity());

        chatroomViewModel =
                ViewModelProviders.of(this).get(ChatroomViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView mygroup_list =(ListView) root.findViewById(R.id.listview2);


        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lists);
        mygroup_list.setAdapter(adapter);

        user =iAccountController.getCurrentUser();
        chatGroupController.getMyJoinedGroupList(new UserAction<>(user, new ActionResultHandler<List<ChatGroup>, String>() {
            @Override
            public void onSuccess(List<ChatGroup> chatGroups) {
                lists.clear();
                for (ChatGroup chatGroup:chatGroups){
                    lists.add(chatGroup.groupName);
                }
                chatGroupController.getMyCreatedGroupList(new UserAction<>(user, new ActionResultHandler<List<ChatGroup>, String>() {
                    @Override
                    public void onSuccess(List<ChatGroup> chatGroups) {
                        for(ChatGroup chatGroup:chatGroups){
                            lists.add(chatGroup.groupName);
                        }
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               for(String string: lists) {
                                   Log.d("lists item", string);
                               }
                               adapter.notifyDataSetChanged();
                           }
                       });
                    }

                    @Override
                    public void onError(String s) {

                    }
                }));
            }

            @Override
            public void onError(String s) {

            }
        }));
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