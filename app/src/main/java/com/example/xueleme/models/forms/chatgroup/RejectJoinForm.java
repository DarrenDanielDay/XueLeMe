package com.example.xueleme.models.forms.chatgroup;

import com.example.xueleme.models.ReflectiveJSONModel;

public class RejectJoinForm extends ReflectiveJSONModel<RejectJoinForm> {
    // 拒绝申请的处理者ID
    public Integer userId;
    // 加群申请的ID
    public Integer requestId;
}
