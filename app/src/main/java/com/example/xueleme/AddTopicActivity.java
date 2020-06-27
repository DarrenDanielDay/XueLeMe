package com.example.xueleme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.topic.CreateTopicForm;

import java.util.ArrayList;
import java.util.List;

public class AddTopicActivity extends AppCompatActivity {

    private IAccountController accountController = new AccountController(this);
    ITopicController topicController = new TopicController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        EditText editText1 = findViewById(R.id.add_topic_et1);
        EditText editText2 = findViewById(R.id.add_topic_et2);
        Button btn1 = findViewById(R.id.add_topic_btn1);

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
                CreateTopicForm createTopicForm = new CreateTopicForm();
                createTopicForm.title = title;
                createTopicForm.content = content;
                createTopicForm.userId = accountController.getCurrentUser().id;
                createTopicForm.tags = new ArrayList<>();
                createTopicForm.images = new ArrayList<>();
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