package com.example.xueleme.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import com.example.xueleme.LoginActivity;
import com.example.xueleme.MyDatabaseHelper;
import com.example.xueleme.R;
import com.example.xueleme.Adapter.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import FunctionPackge.Groupkey;
import FunctionPackge.Task;
import interface_packge.RequestHandler;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyDatabaseHelper dbHelper;
    private List<Task> dataList= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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