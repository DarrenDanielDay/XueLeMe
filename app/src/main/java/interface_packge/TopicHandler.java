package interface_packge;

import FunctionPackge.TopicMessage;

public interface TopicHandler {
    public void GetTopicSuccess(TopicMessage topicMessage);
    public void GetTopicFailed();
}
