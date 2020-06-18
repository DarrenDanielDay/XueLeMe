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

import java.util.List;

import FunctionPackge.Posting;


public class PostingAdapter extends ArrayAdapter<Posting> {
    private int resourceId;
    public PostingAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Posting> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Posting posting = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView taskName = view.findViewById(R.id.item_name);
        taskName.setText(posting.title);
        return view;
    }

}
