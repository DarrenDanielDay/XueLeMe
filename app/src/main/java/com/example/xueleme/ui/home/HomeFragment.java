package com.example.xueleme.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.xueleme.AddTaskActivity;
import com.example.xueleme.MyDatabaseHelper;
import com.example.xueleme.R;
import com.example.xueleme.Adapter.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import FunctionPackge.Task;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyDatabaseHelper dbHelper;
    private List<Task> dataList= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initTask();
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
        FloatingActionButton btn_fab = root.findViewById(R.id.floatingActionButton1);
        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
//            }
//        });
        });
//        String data = new Task(user)
        TaskAdapter adapter = new TaskAdapter(getActivity(), R.layout.task_item, dataList);
        ListView listView = root.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task data = dataList.get(i);
                Toast.makeText(getActivity(), data.start_date + " " + data.start_time + "到" + data.end_time, Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }
    private void initTask() {
        dbHelper = new MyDatabaseHelper(getActivity(), "Task.db", null, 2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Task", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
                String end_time = cursor.getString(cursor.getColumnIndex("end_time"));
                dataList.add(new Task(content, start_date, start_time, end_time));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}