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
import com.example.xueleme.models.locals.Reply;

import java.util.List;

import FunctionPackge.Posting;

public class InvitationAdapter extends BaseAdapter {
    private List<Reply>replyList;
    private LayoutInflater inflater;

    public InvitationAdapter(List<Reply>replyList,Context context){
        this.replyList=replyList;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return replyList ==null?0:replyList.size();
    }

    @Override
    public Reply getItem(int position) {
        return replyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =inflater.inflate(R.layout.reply_item,null);
        Reply mReply = (Reply) getItem(position);
        String uname=new String();
        TextView user_name =(TextView) view.findViewById(R.id.reply_user);
        TextView reply_content =(TextView) view.findViewById(R.id.reply_content);
        TextView quote =(TextView) view.findViewById(R.id.quote);
        user_name.setText(uname);
        reply_content.setText(mReply.content.text);
        return null;
    }
}
