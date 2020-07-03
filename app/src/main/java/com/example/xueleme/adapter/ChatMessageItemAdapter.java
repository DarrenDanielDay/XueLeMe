package com.example.xueleme.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.ChatRecord;
import com.example.xueleme.utils.ImageHelper;
import com.example.xueleme.utils.PixelHelper;

import java.util.List;

public class ChatMessageItemAdapter extends BaseAdapter {

    IAccountController accountController;
    public final List<ChatMessage> messageList;
    private final Context context;
    public ChatMessageItemAdapter(@NonNull Context context, @NonNull List<ChatMessage> objects) {
        accountController = new AccountController(context);
        this.context = context;
        messageList = objects;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messageList.get(position).id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        Log.d(getClass().getSimpleName(), "with message" + this.messageList.get(position).content);
        if (convertView == null) {
            Log.d(getClass().getSimpleName(), "inflate");
            view = LayoutInflater.from(getContext()).inflate(R.layout.chat_message_item, null);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.userNameView = view.findViewById(R.id.user_name);
            holder.userAvatarView = view.findViewById(R.id.user_avatar);
            holder.itemLineLayout = view.findViewById(R.id.chat_message_item_line);
            setItemContentView(holder, getItem(position));
        } else {
            Log.d(getClass().getSimpleName(), "use cache");
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        return view;
    }

    private void setItemContentView(ViewHolder holder, ChatMessage message) {
        Drawable drawable;
        Integer currentUserId = accountController.getCurrentUser().id;
        if (currentUserId.equals(message.senderId)) {
            // 加载自己的蓝色背景
            drawable = getContext().getDrawable(R.drawable.my_chat_text_board);
            // 自己的是从右到左排列
            holder.itemLineLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else {
            // 加载其他人的灰色背景
            drawable = getContext().getDrawable(R.drawable.other_chat_text_board);
            // 其他人的是从左到右排列
            holder.itemLineLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        holder.userNameView.setText(message.senderName);
        ImageHelper.setImageView((Activity) getContext(), holder.userAvatarView, message.senderAvatar);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = PixelHelper.dip2px(this.context, 12);
        params.setMargins(margin, margin, margin, margin);
        if (message.type == ChatRecord.MessageType.TEXT) {
            // 动态创建TextView控件并加到LinearLayout中
            TextView textView = new TextView(getContext());
            textView.setText(message.content);
            textView.setBackground(drawable);
            textView.setTextSize(24);
            textView.setLayoutParams(params);
            holder.itemLineLayout.addView(textView);
        } else if (message.type == ChatRecord.MessageType.IMAGE) {
            // 动态创建ImageView控件并加到LinearLayout中
            ImageView imageView = new ImageView(getContext());
            imageView.setMaxHeight(PixelHelper.dip2px(getContext(), 300));
            imageView.setMaxWidth(PixelHelper.dip2px(getContext(), 300));
            imageView.setBackground(drawable);
            imageView.setLayoutParams(params);
            ImageHelper.setImageView((Activity) getContext(), imageView, message.content);
            holder.itemLineLayout.addView(imageView);
        }
    }

    public Context getContext() {
        return context;
    }

    public static final class ViewHolder {
        public TextView userNameView;
        public ImageView userAvatarView;
        public LinearLayout itemLineLayout;
    }
}
