package com.example.xueleme.business;

import com.example.xueleme.models.forms.account.ChangeAvatarForm;
import com.example.xueleme.models.forms.account.ChangeNicknameForm;
import com.example.xueleme.models.forms.account.ForgetPasswordForm;
import com.example.xueleme.models.forms.account.LoginForm;
import com.example.xueleme.models.forms.account.RegisterForm;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.UserDetail;

public interface IAccountController {
    void register(UserAction<RegisterForm, String, String> action);
    void login(UserAction<LoginForm, String, String> action);
    void forgetPassword(UserAction<ForgetPasswordForm, String, String> action);
    void changeNickname(UserAction<ChangeNicknameForm, String, String> action);
    void changeAvatar(UserAction<ChangeAvatarForm, String, String> action);
    void queryUserDetailFromId(UserAction<Integer, UserDetail, String> action);

    /**
     * 获取当前用户
     * @return  当前用户，如果为null则为未登录
     */
    User getCurrentUser();
}
