package interface_packge;

import FunctionPackge.ChatMessage;

public interface ChatHubHandler {
    public void onReceiveMessage(ChatMessage message);//用户在收到的信息不同的时候采取的动作
    public void onNoConnection();//没有连接
    public void onConnectionFailed(Throwable e);//连接失败
    public void onConnected();
    public void onSendResult(String resultMessage);
    public void onJoinRoomResult(String resultMessage);
    public void onDisconnected();
}
