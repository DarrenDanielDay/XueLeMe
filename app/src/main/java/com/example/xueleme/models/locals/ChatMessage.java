package com.example.xueleme.models.locals;

import com.example.xueleme.models.databases.Column;
import com.example.xueleme.models.databases.DatabaseEntity;
import com.example.xueleme.models.databases.PrimaryKey;
import com.example.xueleme.models.responses.ChatMessageDetail;

import java.util.Date;

@DatabaseEntity
public class ChatMessage {
    @PrimaryKey(autoIncrement = false)
    @Column
    public Integer id;
    @Column
    public Integer senderId; // 发送方用户ID
    @Column
    public Integer groupId; // 群聊ID
    @Column
    public ChatRecord.MessageType type; // 0表示content是文字，1表示content是图片的md5，可请求文件相关api获得具体的图片
    @Column
    public String content;
    @Column
    public Date createdTime; // 消息发送的时间，发送时不需要自己赋值，接收到的消息有时间信息
    @Column
    public String senderName;
    @Column
    public String groupName;
    @Column
    public String senderAvatar;
    public static ChatMessage fromDetail(ChatMessageDetail detail) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id = detail.id;
        chatMessage.type = ChatRecord.MessageType.values()[detail.messageType];
        chatMessage.groupName = detail.group.name;
        chatMessage.content = detail.content;
        chatMessage.senderName = detail.user.nickname;
        chatMessage.createdTime = detail.createdTime;
        chatMessage.groupId = detail.group.id;
        chatMessage.senderId = detail.user.id;
        chatMessage.senderAvatar = detail.user.avatar;
        return chatMessage;
    }
}
