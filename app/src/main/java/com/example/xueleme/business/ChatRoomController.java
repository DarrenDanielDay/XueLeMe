package com.example.xueleme.business;

import com.example.xueleme.models.forms.chatgroup.SendMessageForm;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.utils.FormatHelper;

public class ChatRoomController extends RequestController implements IChatRoomController {
    @Override
    public void subscribe(Subscriber<ChatMessage> subscriber) {
        synchronized (NotificationHub.class) {
            NotificationHub.getInstance().chatMessagePublisher.attach(subscriber);
        }
    }

    @Override
    public void send(UserAction<SendMessageForm, String, String> action) {
        synchronized (NotificationHub.class) {
            NotificationHub.getInstance().joinAsUser(action.data.userId, new ActionResultHandler<String, Throwable>() {
                @Override
                public void onSuccess(String s) {
                    handlePostAction(action, "api/ChatGroup/Message/Send", ServiceResultEnum.SUCCESS);
                }

                @Override
                public void onError(Throwable throwable) {
                    action.resultHandler.onError(FormatHelper.exceptionFormat(throwable));
                }
            });
        }
    }
}
