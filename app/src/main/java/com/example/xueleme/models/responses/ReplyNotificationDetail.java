package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

public class ReplyNotificationDetail extends ReflectiveJSONModel<ReplyNotificationDetail> {
    public Integer topicId;
    public TextAndImageContentDetail replyDetail;
}
