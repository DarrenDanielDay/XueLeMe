package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.UserDetail;

public class User {
    public Integer id;
    public String nickname;
    public String avatar;
    public static User fromDetail(UserDetail userDetail) {
        User user = new User();
        user.avatar = userDetail.avatar;
        user.nickname = userDetail.nickname;
        user.id = userDetail.id;
        return user;
    }
}
