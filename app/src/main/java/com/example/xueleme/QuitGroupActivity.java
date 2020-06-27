package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.xueleme.models.forms.chatgroup.ChangeGroupNameForm;
import com.example.xueleme.models.forms.chatgroup.QuitGroupForm;

public class QuitGroupActivity extends AppCompatActivity {
    private IAccountController accountController = new AccountController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_group);
        Button changeNameButton = findViewById(R.id.change_name);
        Button quitGroupButton = findViewById(R.id.q_group);
        EditText editText = findViewById(R.id.new_name);
        String newName = editText.getText().toString();
        Intent intent = getIntent();
        Integer groupId = intent.getIntExtra("groupId", 0);
        quitGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuitGroupForm quitGroupForm = new QuitGroupForm();
                quitGroupForm.groupId = groupId;
                quitGroupForm.userId = accountController.getCurrentUser().id;
                IChatGroupController chatGroupController = new ChatGroupController();
                chatGroupController.quitGroup(new UserAction<>(quitGroupForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Looper.prepare();
                        Toast.makeText(QuitGroupActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(QuitGroupActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
            }


        });
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeGroupNameForm changeGroupNameForm = new ChangeGroupNameForm();
                changeGroupNameForm.groupId = groupId;
                changeGroupNameForm.newName = newName;
                IChatGroupController chatGroupController = new ChatGroupController();
                chatGroupController.changeGroupName(new UserAction<>(changeGroupNameForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        runOnUiThread(() -> Toast.makeText(QuitGroupActivity.this, s, Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onError(String s) {
                        runOnUiThread(() -> Toast.makeText(QuitGroupActivity.this, "修改群名失败：" + s, Toast.LENGTH_LONG).show());
                    }
                }));
            }
        });
    }
}