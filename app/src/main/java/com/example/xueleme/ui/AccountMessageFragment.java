package com.example.xueleme.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xueleme.MainActivity;
import com.example.xueleme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountMessageFragment extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_account,container,false);
                listView=root.findViewById(R.id.line1);
                final int[]heads=new int[]{R.drawable.ic_baseline_person_pin_24,R.drawable.ic_baseline_create_24,R.drawable.ic_baseline_comment_24,R.drawable.ic_baseline_cancel_24};
                final String[] strings={"头像","修改昵称","评论","退出登录"};
                List list= new ArrayList();
                for(int i=0;i<strings.length;i++)
                {
                    Map<String,Object> map=new HashMap<>();
                    map.put("icon",heads[i]);
                    map.put("content",strings[i]);
                    list.add(map);
                }
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),list,R.layout.item_account,new String[]{"icon","content"},new int[]{R.id.imageView,R.id.textView5});
                listView.setAdapter(simpleAdapter);
                return root;
    }
}
