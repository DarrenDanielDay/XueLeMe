package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GroupChat extends Activity {
    private TextView group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat);
        TextView textView =findViewById(R.id.Gname);
        group_name = findViewById(R.id.Gname);
        Intent intent1 =getIntent();
        String G_name = intent1.getStringExtra("groupname");
        textView.setText(G_name);
    }

}
