package FunctionPackge;

import java.util.Date;

public class ChatMessage {
    public int senderId; // 发送方用户ID
    public int groupId; // 群聊ID
    public int type; // 0表示content是文字，1表示content是图片的md5，可请求文件相关api获得具体的图片
    public String content;
    public Date createdTime; // 消息发送的时间，发送时不需要自己赋值，接收到的消息有时间信息
}
