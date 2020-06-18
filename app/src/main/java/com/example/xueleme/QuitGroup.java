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
               LoginActivity.users.QuitGroup(g_id, new PostmethodHandler() {
                   @Override
                   public void postsuccess() {
                       Looper.prepare();
                       Toast.makeText(QuitGroup.this,"退群成功",Toast.LENGTH_LONG).show();
                       Map map=LoginActivity.users.getChatGroupMap();
                       Iterator<Map.Entry<Groupkey,Integer>> iter = map.entrySet().iterator();

                       while(((Iterator) iter).hasNext()){
                           Map.Entry<Groupkey,Integer> entry = iter.next();
                           Groupkey key = entry.getKey();
                           Integer value = entry.getValue();
                           Log.d("sfsfsfsf","所有的群："+key.groupName+"$$$$$$"+key.groupId+"&&&&&&"+value);
                       }
                       Looper.loop();
                   }

                   @Override
                   public void postfailed() {
                       Looper.prepare();
                       Toast.makeText(QuitGroup.this,"退群失败",Toast.LENGTH_LONG).show();
                       Looper.loop();
                   }

                   @Override
                   public void JSONERROR() {
                       Toast.makeText(QuitGroup.this,"退群失败",Toast.LENGTH_LONG).show();
                   }

                   @Override
                   public void ISNULL() {

                   }
               });
            }
        });
        btn_ctlname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LoginActivity.users.ChangeGroupName(g_id,newname, new PostmethodHandler() {
                @Override
                public void postsuccess() {
                    Looper.prepare();
                    Toast.makeText(QuitGroup.this,"修改成功",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void postfailed() {
                    Looper.prepare();
                    Toast.makeText(QuitGroup.this,"请求失败",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void JSONERROR() {
                    Toast.makeText(QuitGroup.this,"请求失败",Toast.LENGTH_LONG).show();
                }

                @Override
                public void ISNULL() {
                    Toast.makeText(QuitGroup.this,"群聊名不可为空",Toast.LENGTH_LONG).show();
                }
            });
            }
        });
    }
}