package com.example.xueleme.models.forms.chatgroup;

import com.example.xueleme.models.ReflectiveJSONModel;

public class SendMessageForm extends ReflectiveJSONModel<SendMessageForm> {
    public Integer userId;
    public Integer chatGroupId;
    public String messageContent;
    public Integer messageType;
}
