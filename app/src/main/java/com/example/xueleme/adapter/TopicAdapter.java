package com.example.xueleme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xueleme.R;
import com.example.xueleme.TopicActivity;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.FileController;
import com.example.xueleme.business.IFileController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.topic.ViewHolder;
import com.example.xueleme.models.locals.Topic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import FunctionPackge.FileTool;


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
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.title=view.findViewById(R.id.topic_item_tv1);
        viewHolder.content= view.findViewById(R.id.topic_item_tv2);
        viewHolder.title.setText(topic.title);
        String s = topic.content.text.split("\n")[0];
        viewHolder.content.setText(s.substring(0, Math.min(20, s.length())));
        viewHolder.imageView1=view.findViewById(R.id.imageView7);
        viewHolder.imageView2=view.findViewById(R.id.imageView8);
        viewHolder.imageView3=view.findViewById(R.id.imageView9);
        viewHolder.imageView1.setVisibility(View.GONE);
        viewHolder.imageView2.setVisibility(View.GONE);
        viewHolder.imageView3.setVisibility(View.GONE);
        //图片的适配部分
        List <ImageView>list=new ArrayList();
        list.add(viewHolder.imageView1);
        list.add( viewHolder.imageView2);
        list.add(viewHolder.imageView3);
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        Map mapper= (Map) msg.obj;
                        Bitmap bitmap= (Bitmap) mapper.get("img");
                        int id=(int)mapper.get("id");
                        list.get(id).setImageBitmap(bitmap);
                        list.get(id).setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        };
        int length=topic.content.images.size();//总共几张图片
        for(int i=0;i<length;i++)
        {
            String img=topic.content.images.get(i);
            IFileController iFileController=new FileController(getContext());
            int finalI = i;
            iFileController.getFile(new UserAction<>(img, new ActionResultHandler<File, String>() {
                @Override
                public void onSuccess(File file) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                    Map map=new HashMap();
                    map.put("id",finalI);
                    map.put("img",bitmap);
                    Message message=new Message();
                    message.what = 1;
                    message.obj = map;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(String s) {

                }
            }));
        }
        return view;
    }

}
