package com.example.xueleme.models.locals;

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
}
