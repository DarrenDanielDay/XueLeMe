package com.example.xueleme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueleme.adapter.ChatMessageItemAdapter;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatRoomController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatRoomController;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.databases.DatabaseAccessor;
import com.example.xueleme.models.databases.MyEntityDatabaseHelper;
import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChatRoomActivity extends Activity {

    private EditText message;
    private List<ChatMessage> messages;
    private ChatGroup group = new ChatGroup();
    private IAccountController accountController;
    private IChatRoomController chatRoomController = new ChatRoomController();
    private Subscriber<ChatMessage> messageSubscriber;
    private ChatMessageItemAdapter messageAdapter;
    private SendMessageForm sendMessageForm;
    private DatabaseAccessor<ChatMessage> accessor;
    private MyEntityDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        accountController = new AccountController(this);
        accountController.getCurrentUser();
        accessor = new DatabaseAccessor<>(ChatMessage.class);
        helper = new MyEntityDatabaseHelper(this);
        ListView listView = findViewById(R.id.main_listView);
        message = findViewById(R.id.main_content);
        ImageButton imageButton = findViewById(R.id.q_group);
        Button button = findViewById(R.id.main_send);
        TextView groupNameView = findViewById(R.id.Gname);
        Intent intent = getIntent();
        group.groupName = intent.getStringExtra("groupName");
        group.id = intent.getIntExtra("groupId", 0);
        messages = accessor.fetchAll(helper.getReadableDatabase().query(accessor.tableName, null, "groupId = ?", new String[]{group.id.toString()}, null,null,null, "100"));
        Log.d(getClass().getSimpleName(), "Intent: groutName = " + group.groupName + " groupId = " + group.id);
        groupNameView.setText(group.groupName);
        messageAdapter = new ChatMessageItemAdapter(ChatRoomActivity.this, messages);
        listView.setAdapter(messageAdapter);
        messageSubscriber = new Subscriber<>(new Consumer<ChatMessage>() {
            @Override
            public void accept(ChatMessage chatMessage) {
                if (!chatMessage.groupId.equals(group.id)) {
                    return;
                }
                messages.add(chatMessage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("message adapter", "更新界面");
                        Log.d("界面应该有这么多个元素", Integer.toString(messages.size()));
                        for (ChatMessage message : messages) {
                            Log.d("消息内容", message.content);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        chatRoomController.subscribe(messageSubscriber);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomActivity.this, QuitGroupActivity.class);
                intent.putExtra("id", group.id);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageForm = new SendMessageForm();
                sendMessageForm.chatGroupId = group.id;
                sendMessageForm.messageContent = message.getText().toString();
                sendMessageForm.userId = accountController.getCurrentUser().id;
                sendMessageForm.messageType = 0; //发送文字消息
                chatRoomController.send(new UserAction<>(sendMessageForm, new ActionResultHandler<String, String>() {
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

    public static Intent startIntent(Context context, Integer groupId, String groupName) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        return intent;
    }
}
