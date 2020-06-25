package com.example.xueleme.business;

import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.User;

public interface IChatRoomController {
    /**
     * 添加接收消息的Subscriber（订阅者），收到消息后其Consumer回调会被调用
     * @param subscriber 订阅者
     */
    void subscribe(Subscriber<ChatMessage> subscriber);

    /**
     * 向服务器的聊天室发送一条实时消息
     * @param message 消息，包含了发送者、发送到的群聊、内容等信息
     */
    void send(UserAction<SendMessageForm, String, String> action);
}
