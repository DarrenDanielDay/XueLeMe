package interface_packge;

import FunctionPackge.ReplyDetails;

public interface ReplyHandler {
    public void GetReplySuccessed(ReplyDetails replyDetails);
    public void GetReplyFailed();
}
