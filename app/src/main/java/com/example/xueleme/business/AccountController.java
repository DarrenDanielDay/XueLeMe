package com.example.xueleme.business;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.xueleme.R;
import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.forms.account.ChangeAvatarForm;
import com.example.xueleme.models.forms.account.ChangeNicknameForm;
import com.example.xueleme.models.forms.account.ForgetPasswordForm;
import com.example.xueleme.models.forms.account.LoginForm;
import com.example.xueleme.models.forms.account.RegisterForm;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.models.responses.UserDetail;
import com.example.xueleme.utils.HttpRequester;

import io.reactivex.functions.Action;

public class AccountController extends RequestController implements IAccountController {
    private User currentUser = null;
    private final Context activity;
    public static final String SHARED_PREFERENCE_FILE = "shared_preferences";
    public static final String USER_ID_KEY = "userId";
    public AccountController(Context activity) {
        this.activity = activity;
    }

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
                                        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
                                        sharedPreferences.edit().putInt(USER_ID_KEY, currentUser.id).apply();
                                        ensureNotificationJoined();
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
    public void logout(UserAction<Integer, String, String> action) {
        currentUser = null;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(USER_ID_KEY, -1).apply();
        NotificationHub.getInstance().disconnect();
        action.resultHandler.onSuccess("退出登录成功");
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
        if (currentUser != null) {
            ensureNotificationJoined();
            return currentUser;
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(USER_ID_KEY, -1);
        if (userId != -1) {
            currentUser = new User();
            currentUser.id = userId;
            queryUserDetailFromId(new UserAction<>(userId, new ActionResultHandler<UserDetail, String>() {
                @Override
                public void onSuccess(UserDetail userDetail) {
                    currentUser.nickname = userDetail.nickname;
                    currentUser.avatar = userDetail.avatar;
                    Log.d("getCurrentUser", "已登录");
//                    Toast.makeText(activity, "已登录", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String s) {
                    currentUser = null;
                    Log.e("getCurrentUser", s);
                }
            }));
        } else {
            Log.d("getCurrentUser", "未登录");
   //         Toast.makeText(activity, "未登录", Toast.LENGTH_LONG).show();
        }
        return currentUser;
    }

    private void ensureNotificationJoined() {
        if (!NotificationHub.getInstance().isJoined()) {
            if (!NotificationHub.getInstance().isConnected()) {
                NotificationHub.getInstance().connect();
            }
            while (!NotificationHub.getInstance().isConnected());
            NotificationHub.getInstance().joinAsUser(currentUser.id);
        }
    }
}
