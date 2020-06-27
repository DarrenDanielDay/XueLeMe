package com.example.xueleme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueleme.adapter.ReplyListAdapter;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.topic.MakeReplyForm;
import com.example.xueleme.models.locals.Reply;
import com.example.xueleme.models.locals.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends Activity {
    private Integer topicId; //帖子id
    private String i;
    private List<String> images;
    private List<ReplyListAdapter.ReplyModel> replyList = new ArrayList<>();
    private ReplyListAdapter replyListAdapter;
    ITopicController topicController;
    IAccountController accountController = new AccountController(this);
    private Integer referenceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invition_content);
        Intent intent = getIntent();
        i = intent.getStringExtra("id");
        topicId = Integer.parseInt(i);
        ListView listView = findViewById(R.id.content1);
        replyListAdapter = new ReplyListAdapter(replyList, TopicDetailActivity.this);
        listView.setAdapter(replyListAdapter);
        EditText replyView = findViewById(R.id.reply);
        TextView publish_content = findViewById(R.id.publish_content);
        TextView publisher = findViewById(R.id.publisher);
        Button btn_send = findViewById(R.id.send_reply);
        images = new ArrayList<>();
        topicController = new TopicController();
        topicController.getTopicDetail(new UserAction<>(topicId, new ActionResultHandler<Topic, String>() {
            @Override
            public void onSuccess(Topic topic) {
                publish_content.setText(topic.content.text);
                publisher.setText(topic.publisher.fakeName);
                int i = 0;
                for (Reply reply : topic.replies) {
                    ReplyListAdapter.ReplyModel replyModel = new ReplyListAdapter.ReplyModel();
                    replyModel.reply = reply;
                    if (reply.referenceId != null) {
                        topicController.getReplyDetail(new UserAction<>(reply.referenceId, new ActionResultHandler<Reply, String>() {
                            @Override
                            public void onSuccess(Reply reply) {
                                replyModel.reference = reply.content.text;
                                replyList.add(replyModel);
                            }

                            @Override
                            public void onError(String s) {

                            }
                        }));
                    } else {
                        replyModel.reference = "";
                        replyList.add(replyModel);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        replyListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        }));
        publish_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceId = null;  //将引用的的帖子id指定为空指针

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                referenceId = replyList.get(position).reply.referenceId;
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeReplyForm makeReplyForm = new MakeReplyForm();
                makeReplyForm.referenceId = referenceId;
                makeReplyForm.userId = accountController.getCurrentUser().id;
                makeReplyForm.content = replyView.getText().toString();
                makeReplyForm.images = images;
                makeReplyForm.topicId = topicId;

                topicController.makeReply(new UserAction<>(makeReplyForm, new ActionResultHandler<Integer, String>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        replyView.setText("");
                        topicController.getTopicDetail(new UserAction<>(topicId, new ActionResultHandler<Topic, String>() {
                            @Override
                            public void onSuccess(Topic topic) {
                                replyList.clear();
                                for (Reply reply : topic.replies) {
                                    ReplyListAdapter.ReplyModel replyModel = new ReplyListAdapter.ReplyModel();
                                    replyModel.reply = reply;
                                    if (reply.referenceId != null) {
                                        topicController.getReplyDetail(new UserAction<>(reply.referenceId, new ActionResultHandler<Reply, String>() {
                                            @Override
                                            public void onSuccess(Reply reply) {
                                                replyModel.reference = reply.content.text;
                                                replyList.add(replyModel);
                                            }

                                            @Override
                                            public void onError(String s) {

                                            }
                                        }));
                                    } else {
                                        replyModel.reference = "";
                                        replyList.add(replyModel);
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        replyListAdapter.notifyDataSetChanged();
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