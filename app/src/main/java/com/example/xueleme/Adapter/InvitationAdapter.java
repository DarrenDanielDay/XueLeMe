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
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.ITopicController;
import com.example.xueleme.business.TopicController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.locals.Reply;

import java.util.List;

import FunctionPackge.Posting;
import FunctionPackge.Topic;

public class InvitationAdapter extends BaseAdapter {
    public static class Invitation{
        public Reply reply;
        public String reference;
    }
    private List<Reply>replyList;
    private LayoutInflater inflater;
    private ITopicController iTopicController;
    private String my_quote;
    private List<Invitation>invitations;
    public InvitationAdapter(List<Invitation>invitations,Context context){
        this.invitations=invitations;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return invitations ==null?0:invitations.size();
    }

    @Override
    public Invitation getItem(int position) {
        return invitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =inflater.inflate(R.layout.reply_item,null);
        Invitation invitation = (Invitation) getItem(position);
        Reply mReply =invitation.reply;
        String mReference = invitation.reference;
        String uname=new String();
        uname =mReply.replier.fakeName;
         my_quote=new String();
         if(mReference != null) {
             my_quote = mReference;
         }
         else {
             my_quote="";
         }
        TextView user_name =(TextView) view.findViewById(R.id.reply_user);
        TextView reply_content =(TextView) view.findViewById(R.id.reply_content);
        TextView quote =(TextView) view.findViewById(R.id.quote);
        user_name.setText(uname);
        reply_content.setText("回复："+mReply.content.text);
        quote.setText(my_quote);
        return view;
    }
}
