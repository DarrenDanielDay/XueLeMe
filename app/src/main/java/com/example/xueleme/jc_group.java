package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

import interface_packge.GroupCreator;
import interface_packge.JoinGroup;

public class jc_group extends AppCompatActivity {
    Button bt_c,bt_j;
    private EditText c_group_name,j_group_id;
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
                LoginActivity.users.setGroupCreator(new GroupCreator() {
                    @Override
                    public void groupNameIsNull() {
                        Toast.makeText(jc_group.this,"昵称不可为空",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void JSON_ERROR() {
                        Toast.makeText(jc_group.this,"群聊昵称不可为空",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void requestSuccess() {
                        Looper.prepare();
                        Toast.makeText(jc_group.this,"创建群聊成功",Toast.LENGTH_LONG).show();
                        Map<Groupkey, Integer> map=LoginActivity.users.getChatGroupMap();
                        for (Map.Entry<Groupkey, Integer> entry : map.entrySet()) {
                            Groupkey key = entry.getKey();
                            Integer value = entry.getValue();
                        }
                        Looper.loop();
                    }

                    @Override
                    public void requestFailed() {
                        Looper.prepare();
                        Toast.makeText(jc_group.this,"请求失败",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
                LoginActivity.users.CreatGroup(creat_name);
            }
        });
       bt_j.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               Integer join_id =Integer.parseInt(j_group_id.getText().toString());

               LoginActivity.users.setJoinGroup(new JoinGroup() {
                   @Override
                   public void GroupNotExist() {
                       Looper.prepare();
                       Toast.makeText(jc_group.this,"群聊不存在",Toast.LENGTH_LONG).show();
                       Looper.loop();
                   }

                   @Override
                   public void AlreadyInGroup() {
                       Toast.makeText(jc_group.this,"已在群聊中",Toast.LENGTH_LONG).show();
                   }

                   @Override
                   public void HasRequested() {
                       Looper.prepare();
                       Toast.makeText(jc_group.this,"您已经申请过了",Toast.LENGTH_LONG).show();
                       Looper.loop();
                   }

                   @Override
                   public void JSON_ERROR() {
                       Toast.makeText(jc_group.this,"请求失败",Toast.LENGTH_LONG).show();
                   }

                   @Override
                   public void requestSuccess() {
                       Looper.prepare();
                       Toast.makeText(jc_group.this,"申请已发送",Toast.LENGTH_LONG).show();
                       Looper.loop();
                   }

                   @Override
                   public void requestFailed() {
                       Looper.prepare();
                       Toast.makeText(jc_group.this,"请求失败",Toast.LENGTH_LONG).show();
                       Looper.loop();
                   }
               });
               LoginActivity.users.JoinGroupRequest(join_id);
           }
       });
    }
}
