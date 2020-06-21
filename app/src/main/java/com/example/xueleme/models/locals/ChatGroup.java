package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.GroupDetail;
import com.example.xueleme.models.responses.UserDetail;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {
    public Integer id;
    public String groupName;
    public User owner;
    public List<User> members;
    public static ChatGroup fromDetail(GroupDetail detail) {
        ChatGroup group = new ChatGroup();
        group.owner = User.fromDetail(detail.owner);
        group.id = detail.id;
        group.groupName = detail.name;
        if (detail.members != null) {
            group.members = new ArrayList<>();
            for (UserDetail userDetail: detail.members) {
                group.members.add(User.fromDetail(userDetail));
            }
        }
        return group;
    }
}
