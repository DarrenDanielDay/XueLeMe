package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.Date;

public class ChatRecordDetail extends ReflectiveJSONModel<ChatRecordDetail> {
    public Integer id;
    public UserDetail sender;
    public GroupDetail group;
    public Date createdTime;
    public Integer messageType;
    public String content;
}
