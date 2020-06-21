package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.TopicDetail;

import java.util.List;

public class Topic {
    public Integer id;
    public String title;
    public AnonymousUser publisher;
    public TextAndImageContent content;
    public List<String> tags;
    public List<Reply> replies;
    public static Topic fromDetail(TopicDetail detail) {
        Topic topic = new Topic();
        topic.content = TextAndImageContent.fromDetail(detail.contentDetail);
        topic.id = detail.id;
        topic.publisher = AnonymousUser.fromDetail(detail.publisherDetail);
        topic.tags = detail.tags;
        topic.title = detail.title;
        return topic;
    }
}
