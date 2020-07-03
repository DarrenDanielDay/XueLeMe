package com.example.xueleme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.xueleme.adapter.ChatMessageItemAdapter;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatRoomController;
import com.example.xueleme.business.FileController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatRoomController;
import com.example.xueleme.business.IFileController;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.databases.DatabaseAccessor;
import com.example.xueleme.models.databases.MyEntityDatabaseHelper;
import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.utils.ImageHelper;
import com.example.xueleme.utils.ToastHelper;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class ChatRoomActivity extends BaseActivity {

    private EditText message;
    private List<ChatMessage> messages;
    private ChatGroup group = new ChatGroup();
    private IAccountController accountController;
    private IChatRoomController chatRoomController = new ChatRoomController();
    private IFileController fileController;
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
        fileController = new FileController(this);
        accountController.getCurrentUser();
        accessor = new DatabaseAccessor<>(ChatMessage.class);
        helper = new MyEntityDatabaseHelper(this);
        ListView listView = findViewById(R.id.main_listView);
        message = findViewById(R.id.main_content);
        ImageButton imageButton = findViewById(R.id.q_group);
        Button button = findViewById(R.id.main_send);
        TextView groupNameView = findViewById(R.id.group_name);
        Intent intent = getIntent();
        group.groupName = intent.getStringExtra("groupName");
        group.id = intent.getIntExtra("groupId", 0);
        // 获取前100条聊天记录
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
                ImageHelper.askImageFile(ChatRoomActivity.this, new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        fileController.postFile(new UserAction<>(file, new ActionResultHandler<String, String>() {
                            @Override
                            public void onSuccess(String s) {
                                SendMessageForm form = new SendMessageForm();
                                form.messageContent = s;
                                form.userId = accountController.getCurrentUser().id;
                                form.messageType = 1;
                                form.chatGroupId = group.id;
                                chatRoomController.send(new UserAction<>(form, new ActionResultHandler<String, String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Log.d("send image message", s);
                                    }

                                    @Override
                                    public void onError(String s) {
                                        ToastHelper.showToastWithLooper(ChatRoomActivity.this, s);
                                    }
                                }));
                            }

                            @Override
                            public void onError(String s) {
                                ToastHelper.showToastWithLooper(ChatRoomActivity.this, s);
                            }
                        }));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
//                        ToastHelper.showToast(ChatRoomActivity.this, String.valueOf(throwable.getMessage()));
                    }
                });
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
                        ToastHelper.showToastWithLooper(ChatRoomActivity.this, s);
                    }
                }));
            }
        });
        messageSubscriber.attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_manage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_info:
                Intent intent = new Intent(ChatRoomActivity.this, GroupInfoActivity.class);
                intent.putExtra("id", group.id);
                startActivity(intent);
            default:
                break;
        }
        return true;
    }

    public static Intent startIntent(Context context, Integer groupId, String groupName) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageSubscriber.detach();
    }
}
