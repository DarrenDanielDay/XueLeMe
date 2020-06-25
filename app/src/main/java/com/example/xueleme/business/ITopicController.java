package com.example.xueleme.business;

import com.example.xueleme.models.forms.topic.CreateTopicForm;
import com.example.xueleme.models.forms.topic.MakeReplyForm;
import com.example.xueleme.models.locals.Topic;
import com.example.xueleme.models.locals.Zone;

import java.util.List;

public interface ITopicController {
    // 创建话题（发帖），成功时得到帖子id
    void createTopic(UserAction<CreateTopicForm, Integer, String> action);
    // 回贴，成功时得到回复id
    void makeReply(UserAction<MakeReplyForm, Integer, String> action);
    // 根据id获得帖子的内容
    void getTopicDetail(UserAction<Integer, Topic, String> action);
    // 获取所有的贴吧
    void getAllZones(ActionResultHandler<List<Zone>, String> handler);
    // 获取某个吧里所有的帖子
    void getAllTopicsOfZone(UserAction<Zone, List<Topic>, String> action);
}
