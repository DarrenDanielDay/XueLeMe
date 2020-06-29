package com.example.xueleme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import FunctionPackge.Task;
import FunctionPackge.Users;
import model.User;

public class AddTaskActivity extends BaseActivity {

    private MyDatabaseHelper dbHelper;
    private EditText e_content, e_start_date, e_start_time, e_end_time;
    private Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        e_content = findViewById(R.id.editText1);
        e_start_date = findViewById(R.id.editText2);
        e_start_time = findViewById(R.id.editText3);
        e_end_time = findViewById(R.id.editText5);
        System.out.println("hhh1");
        dbHelper = new MyDatabaseHelper(this, "Task.db", null, 2);
        System.out.println("hhh2");
        Button btn_confirm = findViewById(R.id.button);
        aSwitch = findViewById(R.id.switch3);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    e_start_time.setText("00:00");
                    e_end_time.setText("23:59");
                }
                else {
                    e_start_time.setText("");
                    e_end_time.setText("");
                }
            }
        });


        ActionBar actionBar = getSupportActionBar();
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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("content", content);
                values.put("start_date", start_date);
                values.put("start_time", start_time);
                values.put("end_time", end_time);
                db.insert("Task", null, values);
                values.clear();
                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);
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