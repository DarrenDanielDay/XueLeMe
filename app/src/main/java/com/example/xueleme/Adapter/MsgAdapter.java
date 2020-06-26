package com.example.xueleme.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xueleme.LoginActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ChatGroupController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IChatGroupController;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.User;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends BaseAdapter {
    Context context;
    List<ChatMessage> chatMessages =new ArrayList<>();
    LayoutInflater inflater;
    private IAccountController accountController;
    private int ViewType;
    public MsgAdapter(Context context,List<ChatMessage>chatMessages) {
        super();
        this.context=context;
        this.chatMessages=chatMessages;
        accountController = new AccountController(this.context);
        inflater =LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return chatMessages == null?0:chatMessages.size();
    }
    @Override
    public  int getItemViewType(int position){

        if(chatMessages.get(position).senderId== accountController.getCurrentUser().id){
            return 1;
        }
        else {
            return 0;
        }
    }
    @Override
    public ChatMessage getItem(int position) {
        return chatMessages == null?null:chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        ChatMessage msg =getItem(position);
        if(convertView==null){
            Log.d("converterView", "是空的");
            holder =new ViewHolder();
            convertView =inflater.inflate(R.layout.group_message,null);
            holder.leftLayout=convertView.findViewById(R.id.left_layout);
            holder.rightLayout=convertView.findViewById(R.id.right_layout);
            holder.LeftMsg=convertView.findViewById(R.id.left_msg);
            holder.RightMsg=convertView.findViewById(R.id.right_msg);
            holder.Leftname=convertView.findViewById(R.id.user_name);
            holder.Rightname=convertView.findViewById(R.id.my_name);
            holder.Leftimg=convertView.findViewById(R.id.user_image);
            holder.Rightimg=convertView.findViewById(R.id.my_image);
            convertView.setTag(holder);
        }
        else {
            Log.d("converterView", "不是空的");
            holder =(ViewHolder)convertView.getTag();
        }
        if(getItemViewType(position)==0){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.LeftMsg.setText(msg.content);
            holder.Leftname.setText(Integer.toString(msg.senderId)); //暂时没有昵称
            // 暂时无头像 holder.Leftimg=
        }
        else if(getItemViewType(position)==1){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.RightMsg.setText(msg.content);
            holder.Rightname.setText(Integer.toString(msg.senderId)); //暂时没有昵称
            // 暂时无头像 holder.Leftimg=
        }
        Log.d("MsGAdapter正在渲染内容", msg.content);
        return convertView;
    }
    private static class ViewHolder{
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView LeftMsg;
        TextView RightMsg;
        TextView Leftname;

        TextView Rightname;
        ImageView Leftimg;
        ImageView Rightimg;
    }
}
