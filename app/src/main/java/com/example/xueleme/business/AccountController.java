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
import com.example.xueleme.utils.FormatHelper;
import com.example.xueleme.utils.HttpRequester;

import io.reactivex.functions.Action;

public class AccountController extends RequestController implements IAccountController {
    private User currentUser = null;
    private final Context activity;
    public static final String SHARED_PREFERENCE_FILE = "shared_preferences";
    public static final String USER_ID_KEY = "userId";
    public static final String USER_MAIL_ADDRESS_KEY = "mailAddress";
    public static final String USER_PASSWORD_KEY = "password";

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
                                        sharedPreferences.edit()
                                                .putInt(USER_ID_KEY, currentUser.id)
                                                .putString(USER_PASSWORD_KEY, action.data.password)
                                                .putString(USER_MAIL_ADDRESS_KEY, action.data.mailAddress).apply();
                                        ensureNotificationJoined(new ActionResultHandler<String, Throwable>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                action.resultHandler.onSuccess(objectServiceResult.detail);
                                            }

                                            @Override
                                            public void onError(Throwable throwable) {
                                                action.resultHandler.onError(FormatHelper.exceptionFormat(throwable));
                                            }
                                        });
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
        synchronized (NotificationHub.class) {
            NotificationHub.getInstance().disconnect();
        }
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
    public synchronized User getCurrentUser() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(USER_ID_KEY, -1);
        String password = sharedPreferences.getString(USER_PASSWORD_KEY, "");
        String mailAddress = sharedPreferences.getString(USER_MAIL_ADDRESS_KEY, "");
        if (userId != -1) {
            currentUser = new User();
            currentUser.id = userId;
            LoginForm form = new LoginForm();
            form.password = password;
            form.mailAddress = mailAddress;
            login(new UserAction<>(form, new ActionResultHandler<String, String>() {
                @Override
                public void onSuccess(String s) {
                    queryUserDetailFromId(new UserAction<>(currentUser.id, new ActionResultHandler<UserDetail, String>() {
                        @Override
                        public void onSuccess(UserDetail userDetail) {
                            currentUser.avatar = userDetail.avatar;
                            currentUser.nickname = userDetail.nickname;
                        }

                        @Override
                        public void onError(String s) {
                            Log.d("getCurrentUser", s);
                        }
                    }));
                }

                @Override
                public void onError(String s) {
                    Log.d("getCurrentUser", s);
                }
            }));
        } else {
            Log.d("getCurrentUser", "未登录");
        }
        return currentUser;
    }

    private void ensureNotificationJoined(ActionResultHandler<String, Throwable> handler) {
        String tag = "ensureNotificationJoined";
        Log.d(tag, "正在确保加入了聊天室");
        if (!NotificationHub.getInstance().isJoined()) {
            Log.d(tag, "没有加入服务器，需加入");
            if (!NotificationHub.getInstance().isConnected()) {
                Log.d(tag, "没有连接服务器，需连接");
                NotificationHub.getInstance().connect(new ActionResultHandler<String, Throwable>() {
                    @Override
                    public void onSuccess(String o) {
                        Log.d(tag, "调用joinAsUser");
                        NotificationHub.getInstance().joinAsUser(currentUser.id, handler);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        handler.onError(throwable);
                    }
                });
            } else {
                Log.d(tag, "调用joinAsUser");
                NotificationHub.getInstance().joinAsUser(currentUser.id, handler);
            }

        } else {
            Log.d("ensureNotificationJoined", "已经加入");
            handler.onSuccess("已经加入");
        }
    }
}
