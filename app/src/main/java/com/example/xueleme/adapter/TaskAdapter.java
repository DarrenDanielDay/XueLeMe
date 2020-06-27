package com.example.xueleme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xueleme.R;

import java.util.List;

import FunctionPackge.Task;

public class TaskAdapter extends ArrayAdapter<Task> {
    private int resourceId;
    public TaskAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Task> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView taskName = view.findViewById(R.id.item_name);
        taskName.setText(task.content);
        return view;
    }
}
