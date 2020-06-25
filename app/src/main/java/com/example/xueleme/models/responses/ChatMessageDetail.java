package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.locals.ChatRecord;

import java.util.Date;

public class ChatMessageDetail extends ReflectiveJSONModel<ChatMessageDetail> {
    public Integer id;
    public UserDetail user;
    public BriefGroup group;
    public String content;
    public Integer messageType;
    public Date createdTime;
}
