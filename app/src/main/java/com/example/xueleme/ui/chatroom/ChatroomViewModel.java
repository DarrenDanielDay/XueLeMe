package com.example.xueleme.ui.chatroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xueleme.Groupkey;
import com.example.xueleme.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.HashMap;
public class ChatroomViewModel extends ViewModel {
    public List<Groupkey> g_list = new ArrayList<>();
    Groupkey groupkey =new Groupkey("测试群",0);
    public ChatroomViewModel() {
        g_list.add(0,groupkey);
        Set<Groupkey> keySet = LoginActivity.users.chatGroupMap.keySet();
        g_list.addAll(keySet);


    }
}
