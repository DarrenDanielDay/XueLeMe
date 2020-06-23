package com.example.xueleme.business;

import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.User;

public interface IChatRoomController {
    /**
     * 与服务器连接，并告诉服务器客户端的用户是谁
     * @param user 当前用户
     */
    void connectToServerAsUser(User user);

    /**
     * 断开与服务器聊天的连接，断开后将不再收到消息,消息订阅者的订阅也会被取消
     */
    void disconnect();

    /**
     * 添加接收消息的“订阅者”，收到消息后onReceive回调会被调用
     * @param subscriber 订阅者回调
     */
    void subscribe(Subscriber<ChatMessage> subscriber);

    /**
     * 向服务器的聊天室发送一条实时消息
     * @param message 消息，包含了发送者、发送到的群聊、内容等信息
     */
    void send(ChatMessage message);
}
