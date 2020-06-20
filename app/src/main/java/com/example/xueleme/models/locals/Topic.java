package com.example.xueleme.models.locals;

import java.util.List;

public class Topic {
    public Integer id;
    public String title;
    public AnonymousUser publisher;
    public TextAndImageContent content;
    public List<String> tags;
    public List<Reply> replies;
}
