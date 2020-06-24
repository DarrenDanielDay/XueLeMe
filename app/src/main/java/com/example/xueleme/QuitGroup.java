package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.chatgroup.ChangeGroupNameForm;
import com.example.xueleme.models.forms.chatgroup.QuitGroupForm;

import java.util.Iterator;
import java.util.Map;

import FunctionPackge.Groupkey;
import interface_packge.PostmethodHandler;

public class QuitGroup extends AppCompatActivity {
    private String newname=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_group);
        Button btn_ctlname =findViewById(R.id.change_name);
        Button btn_quit =findViewById(R.id.q_group);
        EditText editText =findViewById(R.id.new_name);
        newname=editText.getText().toString();
        Intent intent = getIntent();
        String i =intent.getStringExtra("id");
           final Integer g_id =Integer.parseInt(i);
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuitGroupForm quitGroupForm =new QuitGroupForm();
                quitGroupForm.groupId = g_id;
                quitGroupForm.userId =LoginActivity.users.id;
                IChatGroupController chatGroupController =new ChatGroupController();
                chatGroupController.quitGroup(new UserAction<>(quitGroupForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Looper.prepare();
                        Toast.makeText(QuitGroup.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(QuitGroup.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
            }


        });
        btn_ctlname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeGroupNameForm changeGroupNameForm = new ChangeGroupNameForm();
                changeGroupNameForm.groupId = g_id;
                IChatGroupController chatGroupController =new ChatGroupController();
                chatGroupController.changeGroupName(new UserAction<>(changeGroupNameForm, new ActionResultHandler<String, String>() {
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