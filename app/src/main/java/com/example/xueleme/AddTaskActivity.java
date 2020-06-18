package com.example.xueleme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import FunctionPackge.Task;
import FunctionPackge.Users;
import model.User;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        final EditText e_content = findViewById(R.id.editText1);
        final EditText e_start_date = findViewById(R.id.editText2);
        final EditText e_start_time = findViewById(R.id.editText3);
//        final EditText end_date = findViewById(R.id.editText4);
        final EditText e_end_time = findViewById(R.id.editText5);
        Button btn_confirm = findViewById(R.id.button);
        ActionBar actionBar = getSupportActionBar();
        String username = getIntent().getStringExtra("extra").split(" ")[0];
        String password = getIntent().getStringExtra("extra").split(" ")[1];
        final Users users = new Users(username, password);
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_date = e_start_date.getText().toString();
                String start_time = e_start_time.getText().toString();
                String end_time = e_end_time.getText().toString();
                String content = e_content.getText().toString();
                int year = Integer.parseInt(start_date.substring(0, 4));
                int month = Integer.parseInt(start_date.substring(5, 7));
                int day = Integer.parseInt(start_date.substring(8, 10));
                int start_hour = Integer.parseInt(start_time.substring(0, 2));
                int start_min = Integer.parseInt(start_time.substring(3, 5));
                int end_hour = Integer.parseInt(end_time.substring(0, 2));
                int end_min = Integer.parseInt(end_time.substring(3, 5));
                Task task = new Task(AddTaskActivity.this, users);
                task.create(year, month, day, start_hour, start_min, end_hour, end_min, content);
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