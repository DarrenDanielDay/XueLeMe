package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.xueleme.R;
import com.example.xueleme.ui.chatroom.ChatroomFragment;
import com.example.xueleme.ui.chatroom.ChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

import FunctionPackge.ChatMessage;
import FunctionPackge.Groupkey;
import interface_packge.ChatHubHandler;

public class GroupChat extends Activity {

    private TextView group_name;
    private EditText message;
    public static List<ChatMessage> messages= new ArrayList<>();
    private  List<String> content=new ArrayList<>();
    public List<Groupkey> g_list= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat);
        final List <String>lists= new ArrayList<>();
        ListView listView=findViewById(R.id.main_listView);
        message =findViewById(R.id.main_content);
        TextView textView = findViewById(R.id.Gname);
        ImageButton imageButton=findViewById(R.id.q_group);
        Button button=findViewById(R.id.main_send);
        group_name = findViewById(R.id.Gname);
        Intent intent1 = getIntent();
        String i = intent1.getStringExtra("position");
        final int position =Integer.parseInt(i);
        final String mycontent= message.getText().toString();
        textView.setText(ChatroomFragment.g_list.get(position).groupName);


        LoginActivity.users.setHandler(new ChatHubHandler() {
            @Override
            public void onReceiveMessage(ChatMessage message) {
                Log.d("接受到的消息：", "来自用户:" + message.senderId + "群聊：" + message.groupId + "内容" + message.content + "日期时间：" + message.createdTime);
             //  messages.add(message);
            }

            @Override
            public void onNoConnection() {
               Toast.makeText(GroupChat.this, "无网络连接", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionFailed(Throwable e) {
                Toast.makeText(GroupChat.this, "失去连接", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnected() {
                Toast.makeText(GroupChat.this, "连接成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSendResult(String resultMessage) {
                Log.d("发送结果：", resultMessage);
            }

            @Override
            public void onJoinRoomResult(String resultMessage) {
                Log.d("加入服务器结果：", resultMessage);
            }

            @Override
            public void onDisconnected() {
                Looper.prepare();
                Toast.makeText(GroupChat.this, "已断开连接", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        LoginActivity.users.connect();
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              ChatMessage chatMessage=new ChatMessage();
              chatMessage.content=mycontent;
              chatMessage.groupId=ChatroomFragment.g_list.get(position).groupId;
              chatMessage.senderId=LoginActivity.users.getUserid();
              chatMessage.type=0;
              messages.add(chatMessage);
              content.add(chatMessage.content);
              LoginActivity.users.sendMessage(chatMessage);
              message.setText("");
          }
      });
      imageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent =new Intent(GroupChat.this,QuitGroup.class);
              intent.putExtra("id",ChatroomFragment.g_list.get(position).groupId+"");
              startActivity(intent);
          }
      });
       ArrayAdapter<String>adapter =new<String>ArrayAdapter(GroupChat.this,android.R.layout.simple_list_item_1,content);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();     //显示消息
    }

   /* @Override
    protected void onDestroy() {

        super.onDestroy();
        LoginActivity.users.disconnect();
    } */

}
