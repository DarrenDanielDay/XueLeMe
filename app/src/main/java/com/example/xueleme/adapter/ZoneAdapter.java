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
import com.example.xueleme.models.locals.Zone;

import java.util.List;

public class ZoneAdapter extends ArrayAdapter<Zone> {
    private int resourceId;
    public ZoneAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Zone> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Zone zone = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView taskName = view.findViewById(R.id.zone_item_tv);
        taskName.setText(zone.zoneName);
        return view;
    }
}
