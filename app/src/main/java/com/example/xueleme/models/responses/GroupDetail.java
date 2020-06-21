package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;

public class GroupDetail extends ReflectiveJSONModel<GroupDetail> {
    public Integer id;
    public String name;
    public UserDetail owner;
    public List<UserDetail> members;
}
