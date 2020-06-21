package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.ReplyDetail;

public class Reply {
    public Integer id;
    public Integer topicId;
    public AnonymousUser replier;
    public TextAndImageContent content;
    public static Reply fromDetail(ReplyDetail detail) {
        Reply reply = new Reply();
        reply.content = TextAndImageContent.fromDetail(detail.contentDetail);
        reply.id = detail.id;
        reply.replier = AnonymousUser.fromDetail(detail.user);
        reply.topicId = detail.topicId;
        return reply;
    }
}
