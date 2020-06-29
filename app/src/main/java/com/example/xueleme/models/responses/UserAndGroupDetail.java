package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

public class UserAndGroupDetail extends ReflectiveJSONModel<UserAndGroupDetail> {
    public UserDetail user;
    public GroupDetail group;
    public Integer requestId;
}
