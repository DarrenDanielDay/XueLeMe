package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Invition_content extends AppCompatActivity {
    private Integer id;
    private List<Reply> replyList=new ArrayList<>();
    private InvitationAdapter invitationAdapter;
    ITopicController iTopicController =new TopicController();
    IAccountController iAccountController=new AccountController(this);
    private int referenceid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invition_content);
        Intent intent =getIntent();
        //  id=intent.getStringExtra();   通过id获得帖子信息
        ListView listView =findViewById(R.id.content);
        listView.setAdapter(invitationAdapter);
        EditText reply =findViewById(R.id.reply);
        TextView publisher =findViewById(R.id.publisher);
        Button btn_send =findViewById(R.id.send_reply);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MakeReplyForm makeReplyForm=new MakeReplyForm();
            makeReplyForm.referenceId=referenceid;
            makeReplyForm.userId=iAccountController.getCurrentUser().id;
            makeReplyForm.content=reply.getText().toString();
      //      makeReplyForm.topicId=  获取帖子id

                iTopicController.makeReply(new UserAction<>(makeReplyForm, new ActionResultHandler<Integer, String>() {
                    @Override
                    public void onSuccess(Integer integer) {

                    }

                    @Override
                    public void onError(String s) {

                    }
                }));
            }
        });
    }
}