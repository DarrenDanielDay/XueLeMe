package com.example.xueleme.business;

import android.util.Log;

import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.forms.account.ChangeAvatarForm;
import com.example.xueleme.models.forms.account.ChangeNicknameForm;
import com.example.xueleme.models.forms.account.ForgetPasswordForm;
import com.example.xueleme.models.forms.account.LoginForm;
import com.example.xueleme.models.forms.account.RegisterForm;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.models.responses.UserDetail;
import com.example.xueleme.utils.HttpRequester;

public class AccountController extends RequestController implements IAccountController {
    private User currentUser = null;

    @Override
    public void register(UserAction<RegisterForm, String, String> action) {
        handlePostAction(action, "api/Account/MailAuth/Register", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void login(UserAction<LoginForm, String, String> action) {
        HttpRequester.getInstance().post("api/Account/MailAuth/Login", action.data, ServiceResult.noExtra(), new ActionResultHandler<ServiceResult<Object>, String>() {
            @Override
            public void onSuccess(ServiceResult<Object> objectServiceResult) {
                if (!ServiceResultEnum.values()[objectServiceResult.state].equals(ServiceResultEnum.VALID)) {
                    action.resultHandler.onError(objectServiceResult.detail);
                    return;
                }
                HttpRequester.getInstance().get(
                        "api/Account/MailAuth/QueryId?mail=" + action.data.mailAddress,
                        ServiceResult.ofGeneric(Integer.class),
                        new ActionResultHandler<ServiceResult<Integer>, String>() {
                            @Override
                            public void onSuccess(ServiceResult<Integer> integerServiceResult) {
                                queryUserDetailFromId(new UserAction<>(integerServiceResult.extraData, new ActionResultHandler<UserDetail, String>() {
                                    @Override
                                    public void onSuccess(UserDetail userDetail) {
                                        currentUser = User.fromDetail(userDetail);
                                        action.resultHandler.onSuccess(objectServiceResult.detail);
                                    }

                                    @Override
                                    public void onError(String s) {
                                        action.resultHandler.onError(s);
                                    }
                                }));
                            }

                            @Override
                            public void onError(String s) {
                                action.resultHandler.onError(s);
                            }
                        }
                );
            }

            @Override
            public void onError(String s) {
                action.resultHandler.onError(s);
            }
        });
    }

    @Override
    public void forgetPassword(UserAction<ForgetPasswordForm, String, String> action) {
        handlePostAction(action, "api/Account/MailAuth/ForgetPassword", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void changeNickname(UserAction<ChangeNicknameForm, String, String> action) {
        handlePostAction(action, "api/Account/ChangeNickname", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void changeAvatar(UserAction<ChangeAvatarForm, String, String> action) {
        handlePostAction(action, "api/Account/ChangeAvatar", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void queryUserDetailFromId(UserAction<Integer, UserDetail, String> action) {
        handleGetAction(action, "api/Account/Detail/" + action.data, ServiceResultEnum.EXIST, ServiceResult.ofGeneric(UserDetail.class), new ResponseModelAdapter<UserDetail, UserDetail>() {
            @Override
            public UserDetail convert(UserDetail userDetail) {
                return userDetail;
            }
        });
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
