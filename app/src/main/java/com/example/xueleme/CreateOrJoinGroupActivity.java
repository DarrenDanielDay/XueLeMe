package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

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

public class CreateOrJoinGroupActivity extends AppCompatActivity {
    Button bt_c,bt_j;
    private EditText c_group_name,j_group_id;
    private IAccountController accountController = new AccountController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_group);
        bt_c=findViewById(R.id.create_group);
        bt_j=findViewById(R.id.join_group);
        c_group_name=findViewById(R.id.c_name);
        j_group_id=findViewById(R.id.j_id);

        bt_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String creat_name =c_group_name.getText().toString();
                Integer join_id =Integer.parseInt(j_group_id.getText().toString());
                CreateGroupForm createGroupForm =new CreateGroupForm();
                createGroupForm.groupName = creat_name;
                createGroupForm.userId =accountController.getCurrentUser().id;
                JoinGroupForm joinGroupForm =new JoinGroupForm();
                joinGroupForm.groupId=join_id;
                joinGroupForm.userId=accountController.getCurrentUser().id;
                IChatGroupController iChatGroupController =new ChatGroupController();
                iChatGroupController.createGroup(new UserAction<>(createGroupForm, new ActionResultHandler<ChatGroup, String>() {
                    @Override
                    public void onSuccess(ChatGroup chatGroup) {
                        Looper.prepare();
                        Toast.makeText(CreateOrJoinGroupActivity.this,"创建成功",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(CreateOrJoinGroupActivity.this,s,Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
                iChatGroupController.joinGroup(new UserAction<>(joinGroupForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(String s) {

                    }
                }));
           }
       });
    }
}
