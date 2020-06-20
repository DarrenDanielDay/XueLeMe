package com.example.xueleme.models.forms.topic;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;

public class MakeReplyForm extends ReflectiveJSONModel<MakeReplyForm> {
    public Integer userId;
    public Integer topicId;
    public Integer referenceId;
    public String content;
    public List<String> images;
}
