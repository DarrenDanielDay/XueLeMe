package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

public class ReplyDetail extends ReflectiveJSONModel<ReplyDetail> {
    public Integer id;
    public Integer topicId;
    public AnonymousDetail user;
    public TextAndImageContentDetail contentDetail;
}
