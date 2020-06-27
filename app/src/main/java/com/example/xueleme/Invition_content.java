package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueleme.Adapter.InvitationAdapter;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.topic.MakeReplyForm;
import com.example.xueleme.models.locals.Reply;
import com.example.xueleme.models.locals.Topic;
import com.example.xueleme.models.locals.User;

import java.util.ArrayList;
import java.util.List;

public class Invition_content extends Activity {
    private Integer id; //帖子id
    private String i;
    private List<Reply> replyList=new ArrayList<>();
    private InvitationAdapter invitationAdapter;
    ITopicController iTopicController ;
    IAccountController iAccountController=new AccountController(this);
    private Integer referenceid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invition_content);
        Intent intent =getIntent();
        i=intent.getStringExtra("id");
        id =Integer.parseInt(i);
        ListView listView =findViewById(R.id.content);
        invitationAdapter =new InvitationAdapter(replyList,Invition_content.this);
        listView.setAdapter(invitationAdapter);
        EditText reply =findViewById(R.id.reply);
        TextView publish_content =findViewById(R.id.publish_content);
        TextView publisher=findViewById(R.id.publisher);
        Button btn_send =findViewById(R.id.send_reply);
        iTopicController =new TopicController();
        iTopicController.getTopicDetail(new UserAction<>(id, new ActionResultHandler<Topic, String>() {
            @Override
            public void onSuccess(Topic topic) {
                publish_content.setText(topic.content.text);
                publisher.setText(topic.publisher.fakeName);
            }

            @Override
            public void onError(String s) {

            }
        }));
        publish_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceid=null;  //将引用的的帖子id指定为空指针

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                referenceid=replyList.get(position).referenceId;
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MakeReplyForm makeReplyForm=new MakeReplyForm();
            makeReplyForm.referenceId=referenceid;
            makeReplyForm.userId=iAccountController.getCurrentUser().id;
            makeReplyForm.content=reply.getText().toString();
            makeReplyForm.topicId=id;

                iTopicController.makeReply(new UserAction<>(makeReplyForm, new ActionResultHandler<Integer, String>() {
                    @Override
                    public void onSuccess(Integer integer) {
                    reply.setText("");
                    iTopicController.getTopicDetail(new UserAction<>(id, new ActionResultHandler<Topic, String>() {
                        @Override
                        public void onSuccess(Topic topic) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    invitationAdapter.notifyDataSetChanged();
                                }
                            });
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
            }
        });
    }
}