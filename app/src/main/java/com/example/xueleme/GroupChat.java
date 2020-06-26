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


import com.example.xueleme.Adapter.MsgAdapter;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.ChatRoomController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.IChatRoomController;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.ChatRecord;
import com.example.xueleme.ui.chatroom.ChatroomFragment;
import com.example.xueleme.ui.chatroom.ChatroomViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;



import interface_packge.ChatHubHandler;
import com.example.xueleme.models.locals.ChatMessage;
public class GroupChat extends Activity {

    private TextView group_name;
    private EditText message;
    public static List<ChatMessage> messages= new ArrayList<>();

    public List<ChatGroup> g_list= new ArrayList<>();
    private IChatGroupController iChatGroupController =new ChatGroupController();
    private IAccountController iAccountController;
    private IChatRoomController iChatRoomController=new ChatRoomController();
    private Subscriber<ChatMessage>messageSubscriber;
    private MsgAdapter msgAdapter;
    private SendMessageForm sendMessageForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat);

        iAccountController =new AccountController(this);
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
        sendMessageForm =new SendMessageForm();
        iChatGroupController.getMyCreatedGroupList(new UserAction<>(iAccountController.getCurrentUser(), new ActionResultHandler<List<ChatGroup>, String>() {
            @Override
            public void onSuccess(List<ChatGroup> chatGroups) {
                g_list.clear();
                for(ChatGroup chatGroup:chatGroups){
                    g_list.add(chatGroup);
                }
                iChatGroupController.getMyJoinedGroupList(new UserAction<>(iAccountController.getCurrentUser(), new ActionResultHandler<List<ChatGroup>, String>() {
                    @Override
                    public void onSuccess(List<ChatGroup> chatGroups) {
                        for(ChatGroup chatGroup:chatGroups){
                            g_list.add(chatGroup);
                        }
                        textView.setText(g_list.get(position).groupName);
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
        msgAdapter =new MsgAdapter(GroupChat.this,messages);
        listView.setAdapter(msgAdapter);
        messageSubscriber =new Subscriber<>(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatMessage) {
                messages.add(chatMessage);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       msgAdapter.notifyDataSetChanged();
                   }
               });
            }
        });
        iChatRoomController.subscribe(messageSubscriber);


      imageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent =new Intent(GroupChat.this,QuitGroup.class);
              intent.putExtra("id",g_list.get(position).id+"");
              startActivity(intent);
          }
      });
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendMessageForm.chatGroupId=g_list.get(position).id;
            sendMessageForm.messageContent=message.getText().toString();
            sendMessageForm.userId=iAccountController.getCurrentUser().id;
            sendMessageForm.messageType=0; //发送文字消息
            iChatRoomController.send(new UserAction<>(sendMessageForm, new ActionResultHandler<String, String>() {
                @Override
                public void onSuccess(String s) {
                    message.setText("");
                }

                @Override
                public void onError(String s) {

                }
            }));
        }
    });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageSubscriber.detach();
    }
}
