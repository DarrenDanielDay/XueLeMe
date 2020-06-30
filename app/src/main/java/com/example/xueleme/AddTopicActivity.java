package com.example.xueleme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.topic.CreateTopicForm;
import com.example.xueleme.utils.ImageHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import FunctionPackge.FileTool;
import interface_packge.ImagePost;

public class AddTopicActivity extends BaseActivity {
    int state=0;//用于表示进程的临界区
    int pos=0;
    List ava=new ArrayList();
    private IAccountController accountController = new AccountController(this);
    ITopicController topicController = new TopicController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        ImageView imageView1=findViewById(R.id.imageView2);
        ImageView imageView2=findViewById(R.id.imageView3);
        ImageView imageView3=findViewById(R.id.imageView4);
        EditText editText1 = findViewById(R.id.topic_title);
        EditText editText2 = findViewById(R.id.topic_content);
        Button btn1 = findViewById(R.id.add_topic_1);
        Button btn2=findViewById(R.id.add_image);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText1.getText().toString();
                String content = editText2.getText().toString();
                if(title.length()==0||content.length()==0)
                {
                    Toast.makeText(AddTopicActivity.this,"输入不可为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateTopicForm createTopicForm = new CreateTopicForm();
                createTopicForm.title = title;
                createTopicForm.content = content;
                createTopicForm.userId = accountController.getCurrentUser().id;
                createTopicForm.tags = new ArrayList<>();
                createTopicForm.images = ava;
                createTopicForm.zoneId = getIntent().getIntExtra("zoneId", -1);

                topicController.createTopic(new UserAction<>(createTopicForm, new ActionResultHandler<Integer, String>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Intent intent = new Intent(AddTopicActivity.this, TopicActivity.class);
                        intent.putExtra("extra_data", createTopicForm.zoneId);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String s) {

                    }
                }));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==1)
                {
                    Toast.makeText(AddTopicActivity.this,"图片正在传输中，请稍后",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pos==3)
                {
                    Toast.makeText(AddTopicActivity.this,"最多只允许上传3张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                state=1;
                ImageHelper imageHelper=new ImageHelper();
                imageHelper.askImageFile(AddTopicActivity.this, new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                      //选择图片后的步骤：
                        //上传，然后获得MD5，添加到LIST中
                        //显示图片
                        FileTool fileTool=new FileTool();
                        fileTool.postImage(file, new ImagePost() {
                            @Override
                            public void postFailed() {
                                Looper.prepare();
                                Toast.makeText(AddTopicActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                                state=0;
                                Looper.loop();
                            }

                            @Override
                            public void IsNotImage() {
                                Toast.makeText(AddTopicActivity.this,"不是图片，请重新选择",Toast.LENGTH_SHORT).show();
                                state=0;
                            }

                            @Override
                            public void postSuccess(String Md5) {
                                ava.add(Md5);
                                ImageView imageView=new ImageView(AddTopicActivity.this);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                                if(pos==0)
                                {imageView=imageView1;
                                 }
                                else if(pos==1)
                                {imageView=imageView2;
                                    }
                                else if(pos==2)
                                {imageView=imageView3;
                                    }
                                ImageView finalImageView = imageView;
                                AddTopicActivity.this.runOnUiThread(()-> finalImageView.setImageBitmap(bitmap));
                                pos++;
                                state=0;
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        state=0;
                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}