package com.example.xueleme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.chatgroup.AgreeJoinGroupForm;
import com.example.xueleme.models.forms.chatgroup.RejectJoinForm;
import com.example.xueleme.utils.ImageHelper;
import com.example.xueleme.utils.ToastHelper;

public class HandleJoinRequestActivity extends BaseActivity {
    IChatGroupController chatGroupController;
    IAccountController accountController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_join_request);
        chatGroupController = new ChatGroupController();
        accountController = new AccountController(this);
        Intent intent = getIntent();
        Button rejectButton = findViewById(R.id.reject_button);
        Button agreeButton = findViewById(R.id.agree_button);
        TextView userNameText = findViewById(R.id.user_name);
        TextView descriptionText = findViewById(R.id.description);
        ImageView avatar = findViewById(R.id.user_avatar);
        String userName = intent.getStringExtra("userName");
        String avatarMD5 = intent.getStringExtra("avatar");
        String groupName = intent.getStringExtra("groupName");
        Integer userId = intent.getIntExtra("userId", -1);
        Integer requestId =  intent.getIntExtra("requestId", -1);
        Integer groupId = intent.getIntExtra("groupId", -1);
        userNameText.setText(userName);
        ImageHelper.setImageView(this, avatar, avatarMD5);
        descriptionText.setText(String.format(
                "（账号 %d） 申请加入群 %s (群号 %d)",
                userId,
                groupName,
                groupId
               ));
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RejectJoinForm rejectJoinForm = new RejectJoinForm();
                rejectJoinForm.requestId = requestId;
                rejectJoinForm.userId = accountController.getCurrentUser().id;
                chatGroupController.rejectJoin(new UserAction<>(rejectJoinForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        ToastHelper.showToastWithLooper(HandleJoinRequestActivity.this, s);
                        // 显示信息后直接结束本Activity
                        HandleJoinRequestActivity.this.finish();
                    }

                    @Override
                    public void onError(String s) {
                        ToastHelper.showToastWithLooper(HandleJoinRequestActivity.this, s);
                    }
                }));
            }
        });
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgreeJoinGroupForm agreeJoinGroupForm = new AgreeJoinGroupForm();
                agreeJoinGroupForm.requestId = requestId;
                agreeJoinGroupForm.userId = accountController.getCurrentUser().id;
                chatGroupController.agreeJoin(new UserAction<>(agreeJoinGroupForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        ToastHelper.showToastWithLooper(HandleJoinRequestActivity.this, s);
                        // 同意加群后跳转到改群聊
                        HandleJoinRequestActivity.this.finish();
                        startActivity(ChatRoomActivity.startIntent(HandleJoinRequestActivity.this, groupId, groupName));
                    }

                    @Override
                    public void onError(String s) {
                        ToastHelper.showToastWithLooper(HandleJoinRequestActivity.this, s);
                    }
                }));
            }
        });
    }

    public static Intent startIntent(Context context, String userName, String avatar, String groupName, Integer userId, Integer groupId, Integer requestId) {
        Intent intent = new Intent(context, HandleJoinRequestActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("avatar", avatar);
        intent.putExtra("groupName", groupName);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("requestId", requestId);
        return intent;
    }

}