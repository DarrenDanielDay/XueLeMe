package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Lists extends AppCompatActivity {
    private String[] data = {"数学", "英语"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Lists.this, android.R.layout.simple_list_item_1, data);
        ListView listView = findViewById(R.id.list_list_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Lists.this, data[i], Toast.LENGTH_LONG).show();
            }
        });
    }
}