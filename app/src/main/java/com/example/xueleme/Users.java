package com.example.xueleme;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import interface_packge.Forgetpass_interface;
import interface_packge.LoginHandler;
import interface_packge.Register_interface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Users {
    private String account;
    private String password;
    LoginHandler loginHandler;
    Register_interface register_interface;
    Forgetpass_interface forgetpass_interface;
    //构造函数
    public Users(String account, String password) {
        this.account = account;
        this.password = password;
    }
    //get方法
    public String getAccount() {
        return account;
    }
    public String getPassword() {
        return password;
    }
    public void setLoginHandler(LoginHandler handler)
    {
        loginHandler=handler;
    }
    public void setRegister_interface(Register_interface register_interface1){register_interface=register_interface1;}
    public void setForgetpass_interface(Forgetpass_interface forgetpass_interface1){forgetpass_interface=forgetpass_interface1;}
    /*Login()为登录程序，不接受参数，是通过将Users类的account和password属性发到
     *服务器上接受服务器的结果，返回有五种状态下的字符串：
     *1.请输入用户名：account为空
     *2.请输入密码：account不为空，password为空
     *3.请求失败，指当登录程序和服务器连接成功时，返回的code不是200
     *4.密码错误
     *5.密码正确
     *6.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
     * */
    //登录程序
    public void Login() {
        if (account.length() == 0)
        {loginHandler.account_isnull();
            return;
        }
        if (password.length() == 0)
        {loginHandler.password_isnull();
            return;
        }
        String URL = "http://darrendanielday.club/api/Account/MailAuth/Login";
        MediaType mediaType = MediaType.parse("application/json");
        //创建客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        //建立JSON字符串
        JSONObject json_account_message = new JSONObject();
        try{
            json_account_message.put("mailAddress", account);
            json_account_message.put("password", password);}catch (JSONException e) {
            loginHandler.connection_failed();
            return;
        }
        //创建请求体
        RequestBody login_message = RequestBody.create(String.valueOf(json_account_message), mediaType);
        //创建请求
        Request login_request = new Request.Builder()
                .post(login_message)
                .url(URL)
                .build();
        //创建任务并异步传输
        final Call task = client.newCall(login_request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                loginHandler.connection_failed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state= response.body().string();
                String[] login_state=state.split(",");
                String r;
                int code=response.code();
                if(code!=200)
                    loginHandler.connection_failed();
                if(!login_state[0].contains("u"))
                    loginHandler.password_wrong();
                else
                    loginHandler.password_correct();
            }
        });
    }
    //注册程序
    /*
     * 注册程序，返回登录状态字符串
     *1.请输入用户名：account为空
     *2.请输入密码：account不为空，password为空
     *3.连接成功，返回服务器给的注册细节
     * 4.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
     * */
    public void Register(){
        if(account.length()==0)
        {register_interface.account_isnull();
            return;
        }
        if(password.length()==0){
            register_interface.password_isnull();
            return;}
        String URL="http://darrendanielday.club/api/Account/MailAuth/Register";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("mailAddress",account);
            json_account_message.put("password",password);}catch (JSONException e){register_interface.JSON_error();}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                register_interface.request_failed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(state,map.getClass());
                String s= (String) map.get("detail");
                if(s.equals("注册成功，请注意查收邮件"))
                {
                    register_interface.success_register();
                }
                else if(s.equals("邮箱已注册"))
                {
                    register_interface.already_register();
                }
                else
                {
                    forgetpass_interface.request_failed();
                }
            }
        });
    }
    /*忘记密码程序:通过用户名来找回密码
     *1.用户没有输入用户名，返回“请输入用户名”
     *2.返回服务器发送的信息,邮箱未注册、请求成功
     * 3.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
     * */
    public void Forget_password(){
        if(account.length()==0)
        {
            forgetpass_interface.account_isnull();
            return;
        }

        String URL="http://darrendanielday.club/api/Account/MailAuth/ForgetPassword";
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        MediaType mediaType=MediaType.parse("application/json");
        JSONObject jsonObject=new JSONObject();
        try{ jsonObject.put("mailAddress",account);}catch (JSONException e){forgetpass_interface.request_failed();}
        RequestBody requestbody=RequestBody.create(String.valueOf(jsonObject),mediaType);
        Request request=new Request.Builder()
                .post(requestbody)
                .url(URL)
                .build();
        final  Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                forgetpass_interface.request_failed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state=response.body().string();
                Gson gson=new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(state,map.getClass());
                String s=(String) map.get("detail");
                Log.d("sdfs0", String.valueOf(response.code()));
                if(s.equals("邮箱未注册"))
                {
                    forgetpass_interface.account_notexistence();
                }
                else if(s.equals("请求成功"))
                {
                    forgetpass_interface.request_success();
                }
                else
                {
                    forgetpass_interface.request_failed();
                }
            }
        });
    }

}

