package com.example.xueleme.business;

import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;

public class ChatRoomController extends RequestController implements IChatRoomController {
    @Override
    public void subscribe(Subscriber<ChatMessage> subscriber) {
        NotificationHub.getInstance().chatMessagePublisher.attach(subscriber);
    }

    @Override
    public void send(UserAction<SendMessageForm, String, String> action) {
        if(!NotificationHub.getInstance().isConnected()) {
            NotificationHub.getInstance().connect();
        }
        handlePostAction(action, "api/ChatGroup/Message/Send", ServiceResultEnum.SUCCESS);
    }
}
