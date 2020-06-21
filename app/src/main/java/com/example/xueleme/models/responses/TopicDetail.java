package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;

public class TopicDetail extends ReflectiveJSONModel<TopicDetail> {
    public Integer id;
    public String title;
    public AnonymousDetail publisherDetail;
    public Integer zoneId;
    public TextAndImageContentDetail contentDetail;
    public List<String> tags;
}
