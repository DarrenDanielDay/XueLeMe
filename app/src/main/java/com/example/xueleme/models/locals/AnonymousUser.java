package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.AnonymousDetail;

public class AnonymousUser {
    public Integer id;
    public String fakeName;
    public static AnonymousUser fromDetail(AnonymousDetail detail) {
        AnonymousUser anonymousUser = new AnonymousUser();
        anonymousUser.fakeName = detail.fakeName;
        anonymousUser.id = detail.userId;
        return anonymousUser;
    }
}
