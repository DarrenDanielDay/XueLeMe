package com.example.xueleme.ui.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xueleme.ChatRoomActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.CreateOrJoinGroupActivity;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment {

    private IChatGroupController chatGroupController = new ChatGroupController();
    private ArrayAdapter<String> adapter;
    private List<ChatGroup> groups = new ArrayList<>();
    private List<String> groupNames = new ArrayList<>();
    private IAccountController accountController;
    private User user;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountController =new AccountController(getActivity());

        View root = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView myGroupListView = root.findViewById(R.id.listview2);


        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,groupNames);
        myGroupListView.setAdapter(adapter);

        user = accountController.getCurrentUser();
        chatGroupController.getMyJoinedGroupList(new UserAction<>(user, new ActionResultHandler<List<ChatGroup>, String>() {
            @Override
            public void onSuccess(List<ChatGroup> chatGroups) {
                groups.clear();
                groups.addAll(chatGroups);
                chatGroupController.getMyCreatedGroupList(new UserAction<>(user, new ActionResultHandler<List<ChatGroup>, String>() {
                    @Override
                    public void onSuccess(List<ChatGroup> chatGroups) {
                        groups.addAll(chatGroups);
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               groupNames.clear();
                               for(ChatGroup group: groups) {
                                   groupNames.add(group.groupName);
                                   Log.d("lists item", group.groupName);
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
                Intent intent = new Intent(getActivity(), CreateOrJoinGroupActivity.class);
                intent.putExtra("extra", getActivity().getIntent().getStringExtra("extra_data"));
                startActivity(intent);
            }
        });

        myGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 =new Intent(getActivity(), ChatRoomActivity.class);

                intent1.putExtra("groupId", groups.get(position).id);
                intent1.putExtra("groupName", groups.get(position).groupName);
                startActivity(intent1);
            }


        });


        return root;
    }

}