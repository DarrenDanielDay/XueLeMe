package com.example.xueleme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xueleme.R;
import com.example.xueleme.models.locals.Topic;

import java.util.List;

import FunctionPackge.Posting;


public class TopicAdapter extends ArrayAdapter<Topic> {
    private int resourceId;
    public TopicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Topic> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Topic topic = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView taskName = view.findViewById(R.id.item_name);
        taskName.setText(topic.title);
        return view;
    }

}
