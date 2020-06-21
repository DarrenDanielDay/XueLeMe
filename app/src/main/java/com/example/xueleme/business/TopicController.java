package com.example.xueleme.business;

import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.forms.topic.CreateTopicForm;
import com.example.xueleme.models.forms.topic.MakeReplyForm;
import com.example.xueleme.models.locals.Reply;
import com.example.xueleme.models.locals.Topic;
import com.example.xueleme.models.responses.CreatedDetail;
import com.example.xueleme.models.responses.ReplyDetail;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.models.responses.TopicDetail;
import com.example.xueleme.utils.HttpRequester;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class TopicController extends RequestController implements ITopicController {
    @Override
    public void createTopic(UserAction<CreateTopicForm, Integer, String> action) {
        handlePostAction(action, "api/Topic/CreateTopic", ServiceResultEnum.SUCCESS, ServiceResult.ofGeneric(CreatedDetail.class), new ResponseModelAdapter<CreatedDetail, Integer>() {
            @Override
            public Integer convert(CreatedDetail createdDetail) {
                return createdDetail.createdId;
            }
        });
    }

    @Override
    public void makeReply(UserAction<MakeReplyForm, Integer, String> action) {
        handlePostAction(action, "api/Topic/MakeReply", ServiceResultEnum.SUCCESS, ServiceResult.ofGeneric(CreatedDetail.class), new ResponseModelAdapter<CreatedDetail, Integer>() {
            @Override
            public Integer convert(CreatedDetail createdDetail) {
                return createdDetail.createdId;
            }
        });
    }

    @Override
    public void getTopicDetail(UserAction<Integer, Topic, String> action) {
        HttpRequester.getInstance().get("api/Topic/TopicDetail/" + action.data, ServiceResult.ofGeneric(TopicDetail.class), new ActionResultHandler<ServiceResult<TopicDetail>, String>() {
            @Override
            public void onSuccess(ServiceResult<TopicDetail> topicDetailServiceResult) {
                if (!ServiceResultEnum.values()[topicDetailServiceResult.state].equals(ServiceResultEnum.SUCCESS)) {
                    action.resultHandler.onError(topicDetailServiceResult.detail);
                } else {
                    HttpRequester.getInstance().get("api/Topic/RepliesOfTopic/" + topicDetailServiceResult.extraData.id, ServiceResult.listParser(ReplyDetail.class), new ActionResultHandler<ServiceResult<List<ReplyDetail>>, String>() {
                        @Override
                        public void onSuccess(ServiceResult<List<ReplyDetail>> listServiceResult) {
                            Topic topic = Topic.fromDetail(topicDetailServiceResult.extraData);
                            topic.replies = new ArrayList<>();
                            for (ReplyDetail replyDetail: listServiceResult.extraData) {
                                topic.replies.add(Reply.fromDetail(replyDetail));
                            }
                            action.resultHandler.onSuccess(topic);
                        }

                        @Override
                        public void onError(String s) {
                            action.resultHandler.onError(s);
                        }
                    });
                }
            }

            @Override
            public void onError(String s) {
                action.resultHandler.onError(s);
            }
        });
        /*
        handleGetAction(action, "api/Topic/TopicDetail/" + action.data, ServiceResultEnum.EXIST, ServiceResult.ofGeneric(TopicDetail.class), new ResponseModelAdapter<TopicDetail, Topic>() {
            @Override
            public Topic convert(TopicDetail topicDetail) {

                HttpRequester.getInstance();
                return Topic.fromDetail(topicDetail);
            }
        });
        //*/
    }
}
