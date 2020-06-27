package com.example.xueleme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xueleme.R;
import com.example.xueleme.models.locals.Reply;

import java.util.List;

public class ReplyListAdapter extends BaseAdapter {
    public static class ReplyModel {
        public Reply reply;
        public String reference;
    }

    private LayoutInflater inflater;
    private List<ReplyModel> replyModels;

    public ReplyListAdapter(List<ReplyModel> replyModels, Context context) {
        this.replyModels = replyModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return replyModels == null ? 0 : replyModels.size();
    }

    @Override
    public ReplyModel getItem(int position) {
        return replyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.reply_item, null);
        ReplyModel replyModel = (ReplyModel) getItem(position);
        Reply mReply = replyModel.reply;
        String mReference = replyModel.reference;
        String userName;
        userName = mReply.replier.fakeName;
        String my_quote;
        if (mReference != null) {
            my_quote = mReference;
        } else {
            my_quote = "";
        }
        TextView user_name = (TextView) view.findViewById(R.id.reply_user);
        TextView reply_content = (TextView) view.findViewById(R.id.reply_content);
        TextView quote = (TextView) view.findViewById(R.id.quote);
        user_name.setText(userName);
        reply_content.setText("回复：" + mReply.content.text);
        quote.setText(my_quote);
        return view;
    }
}
