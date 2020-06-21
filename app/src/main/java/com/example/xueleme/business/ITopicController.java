package com.example.xueleme.business;

import com.example.xueleme.models.forms.topic.CreateTopicForm;
import com.example.xueleme.models.forms.topic.MakeReplyForm;
import com.example.xueleme.models.locals.Topic;

public interface ITopicController {
    // 创建话题（发帖），成功时得到帖子id
    void createTopic(UserAction<CreateTopicForm, Integer, String> action);
    // 回贴，成功时得到回复id
    void makeReply(UserAction<MakeReplyForm, Integer, String> action);
    // 根据id获得帖子的内容
    void getTopicDetail(UserAction<Integer, Topic, String> action);
}
