package com.example.xueleme.models.forms.topic;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;

public class CreateTopicForm extends ReflectiveJSONModel<CreateTopicForm> {
    public String title;
    public Integer userId;
    public Integer zoneId;
    public List<String> tags;
    public String content;
    // images只需要图片文件的md5即可
    public List<String> images;

}
