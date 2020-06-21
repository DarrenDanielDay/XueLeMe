package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.ChatRecordDetail;

import java.util.Date;

public class ChatRecord {
    public Integer id;
    public User sender;
    public ChatGroup chatGroup;
    public Date createdTime;
    public enum MessageType {
        TEXT,
        IMAGE,
    }
    public MessageType messageType;
    public String content;
    public static ChatRecord fromDetail(ChatRecordDetail detail) {
        ChatRecord record = new ChatRecord();
        record.chatGroup = ChatGroup.fromDetail(detail.group);
        record.content = detail.content;
        record.createdTime = detail.createdTime;
        record.id = detail.id;
        return record;
    }
}
