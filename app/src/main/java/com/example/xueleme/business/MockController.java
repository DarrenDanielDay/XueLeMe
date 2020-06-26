package com.example.xueleme.business;

import com.example.xueleme.models.forms.account.ChangeAvatarForm;
import com.example.xueleme.models.forms.account.ChangeNicknameForm;
import com.example.xueleme.models.forms.account.ForgetPasswordForm;
import com.example.xueleme.models.forms.account.LoginForm;
import com.example.xueleme.models.forms.account.RegisterForm;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.UserDetail;

// Mock接口，可用于界面开发时调用，对接完成以后会有新的实现类，对接时再替换
public class MockController implements IAccountController {
    ServiceResult<Object> basicReturnObject = new ServiceResult<>(Object.class);

    @Override
    public void register(UserAction<RegisterForm, String, String> action) {
        action.resultHandler.onSuccess("注册成功");
    }

    @Override
    public void login(UserAction<LoginForm, String, String> action) {
        // 举个例子，假设登录成功后用户名是邮箱
        currentUser.nickname = action.data.mailAddress;
        currentUser.id = 1;
        action.resultHandler.onSuccess("登录成功");
    }

    @Override
    public void logout(UserAction<Integer, String, String> action) {
        action.resultHandler.onSuccess("退出登录成功");
    }

    @Override
    public void forgetPassword(UserAction<ForgetPasswordForm, String, String> action) {
        action.resultHandler.onSuccess("重置密码成功");
    }

    @Override
    public void changeNickname(UserAction<ChangeNicknameForm, String, String> action) {
        action.resultHandler.onSuccess("修改昵称成功");
    }

    @Override
    public void changeAvatar(UserAction<ChangeAvatarForm, String, String> action) {
        action.resultHandler.onSuccess("修改头像成功");
    }

    @Override
    public void queryUserDetailFromId(UserAction<Integer, UserDetail, String> action) {
        // 想要用测试数据填充界面元素可以在mock接口的时候写入
        UserDetail userDetail = new UserDetail();
        userDetail.avatar = null;
        userDetail.id = action.data;
        userDetail.nickname = "Darren";
        action.resultHandler.onSuccess(userDetail);
    }

    private User currentUser = new User();


    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
