package com.example.xueleme.models.forms.chatgroup;

import com.example.xueleme.models.ReflectiveJSONModel;

public class KickUserForm extends ReflectiveJSONModel<KickUserForm> {
    // 为确认踢人者身份的ID
    public Integer ownerId;
    // 要踢的人的ID
    public Integer userId;
    public Integer groupId;
}
