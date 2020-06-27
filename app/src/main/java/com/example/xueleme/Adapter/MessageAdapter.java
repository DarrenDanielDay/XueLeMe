package com.example.xueleme.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.responses.UserDetail;
import com.example.xueleme.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    Activity activity;
    List<ChatMessage> chatMessages = new ArrayList<>();
    LayoutInflater inflater;
    private IAccountController accountController;

    public MessageAdapter(Activity context, List<ChatMessage> chatMessages) {
        super();
        this.activity = context;
        this.chatMessages = chatMessages;
        accountController = new AccountController(this.activity);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chatMessages == null ? 0 : chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (chatMessages.get(position).senderId == accountController.getCurrentUser().id) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        return chatMessages == null ? null : chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        ChatMessage chatMessage = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.group_message, null);
            holder.leftLayout = convertView.findViewById(R.id.left_layout);
            holder.rightLayout = convertView.findViewById(R.id.right_layout);
            holder.leftMessage = convertView.findViewById(R.id.left_msg);
            holder.rightMessage = convertView.findViewById(R.id.right_msg);
            holder.leftName = convertView.findViewById(R.id.user_name);
            holder.rightName = convertView.findViewById(R.id.my_name);
            holder.leftImage = convertView.findViewById(R.id.user_image);
            holder.rightImage = convertView.findViewById(R.id.my_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == 0) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMessage.setText(chatMessage.content);
            accountController.queryUserDetailFromId(new UserAction<>(chatMessage.senderId, new ActionResultHandler<UserDetail, String>() {
                @Override
                public void onSuccess(UserDetail userDetail) {
                    activity.runOnUiThread(() -> holder.leftName.setText(String.valueOf(userDetail.nickname)));
                    ImageHelper.setImageView(MessageAdapter.this.activity, holder.rightImage, userDetail.avatar);
                }

                @Override
                public void onError(String s) {

                }
            }));
        } else if (getItemViewType(position) == 1) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMessage.setText(chatMessage.content);
            accountController.queryUserDetailFromId(new UserAction<>(chatMessage.senderId, new ActionResultHandler<UserDetail, String>() {
                @Override
                public void onSuccess(UserDetail userDetail) {
                    activity.runOnUiThread(() -> holder.rightName.setText(String.valueOf(userDetail.nickname)));
                    ImageHelper.setImageView(MessageAdapter.this.activity, holder.rightImage, userDetail.avatar);
                }

                @Override
                public void onError(String s) {

                }
            }));
        }
        return convertView;
    }

    private static class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
        TextView leftName;

        TextView rightName;
        ImageView leftImage;
        ImageView rightImage;
    }
}
