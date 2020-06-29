package com.example.xueleme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.chatgroup.CreateGroupForm;
import com.example.xueleme.models.forms.chatgroup.JoinGroupForm;
import com.example.xueleme.models.locals.ChatGroup;

public class CreateOrJoinGroupActivity extends BaseActivity {
    private EditText createGroupNameText, joinGroupIdText;
    private IChatGroupController chatGroupController =new ChatGroupController();
    private IAccountController accountController = new AccountController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_group);
        Button createButton = findViewById(R.id.create_group);
        Button joinButton = findViewById(R.id.join_group);
        Button searchGroupButton = findViewById(R.id.search_group);
        createGroupNameText =findViewById(R.id.c_name);
        joinGroupIdText =findViewById(R.id.j_id);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String createName = createGroupNameText.getText().toString();
                CreateGroupForm createGroupForm = new CreateGroupForm();
                createGroupForm.groupName = createName;
                createGroupForm.userId = accountController.getCurrentUser().id;
                chatGroupController.createGroup(new UserAction<>(createGroupForm, new ActionResultHandler<ChatGroup, String>() {
                    @Override
                    public void onSuccess(ChatGroup chatGroup) {
                        Looper.prepare();
                        Toast.makeText(CreateOrJoinGroupActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(CreateOrJoinGroupActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer joinGroupId = Integer.parseInt(joinGroupIdText.getText().toString());
                JoinGroupForm joinGroupForm = new JoinGroupForm();
                joinGroupForm.groupId = joinGroupId;
                joinGroupForm.userId = accountController.getCurrentUser().id;
                chatGroupController.joinGroup(new UserAction<>(joinGroupForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(String s) {

                    }
                }));
           }
        });
        searchGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateOrJoinGroupActivity.this, SearchAndJoinGroupActivity.class));
            }
        });
    }
}
