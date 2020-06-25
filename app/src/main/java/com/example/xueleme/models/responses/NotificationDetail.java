package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.locals.NotificationTypeEnum;

public class NotificationDetail extends ReflectiveJSONModel<NotificationDetail> {
    public Integer id;
    public String content;
    public Integer notificationTypeEnum;
}
