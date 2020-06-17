package com.example.xueleme.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.xueleme.AddTaskActivity;
import com.example.xueleme.LoginActivity;
import com.example.xueleme.MainActivity;
import com.example.xueleme.R;
import com.example.xueleme.ui.chatroom.ChatroomFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import FunctionPackge.Task;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private String[] data = {"Apple", "Pear"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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
                intent.putExtra("extra", getActivity().getIntent().getStringExtra("extra_data"));
                startActivity(intent);
            }
//            }
//        });
        });
//        String data = new Task(user)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        ListView listView = root.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        return root;
    }
}