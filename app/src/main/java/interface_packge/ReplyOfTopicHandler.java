package interface_packge;

import java.util.List;

import FunctionPackge.ReplyDetails;

public interface ReplyOfTopicHandler {
    public void GetReplyOfTopicSucess(List<ReplyDetails> replyDetailsList);
    public void GetReplyOfTopicFailed();
}
