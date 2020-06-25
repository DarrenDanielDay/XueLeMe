package com.example.xueleme.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xueleme.R;

import java.util.List;

import FunctionPackge.Posting;
import model.Reply;
import model.ReplyAt;

public class InvitationAdapter extends BaseAdapter {
    private List<ReplyAt>replyAtList;
    private LayoutInflater inflater;
    private ReplyAt a;

    public InvitationAdapter(List<ReplyAt>replyAtList,Context context){
        this.replyAtList=replyAtList;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return replyAtList ==null?0:replyAtList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyAtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =inflater.inflate(R.layout.reply_item,null);
        ReplyAt mRelyAt = (ReplyAt)getItem(position);
        String uname=new String();
        TextView user_name =(TextView) view.findViewById(R.id.reply_user);
        TextView reply_content =(TextView) view.findViewById(R.id.reply_content);
        user_name.setText(uname);
        reply_content.setText(mRelyAt.reply.replyContent.text);
        return null;
    }
}
