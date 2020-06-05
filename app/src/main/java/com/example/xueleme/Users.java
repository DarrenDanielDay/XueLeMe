package com.example.myapplication;
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
import org.jsoup.helper.HttpConnection;
import org.w3c.dom.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
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
    public String Login() throws JSONException, IOException, ExecutionException, InterruptedException {
        if(account.length()==0)
            return "请输入用户名";
        if(password.length()==0)
            return "请输入密码";
        String URL="http://darrendanielday.club/api/Account/MailAuth/Login";
        MediaType mediaType=MediaType.parse("application/json");
        //创建客户端
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        //建立JSON字符串
        JSONObject json_account_message=new JSONObject();
        json_account_message.put("mailAddress",account);
        json_account_message.put("password",password);
        //创建请求体
        RequestBody login_message=RequestBody.create(String.valueOf(json_account_message),mediaType);
        //创建请求
        Request login_request=new Request.Builder()
                .post(login_message)
                .url(URL)
                .build();
        //创建任务并同步传输
         final Call task=client.newCall(login_request);
         Callable<String> callable=new Callable<String>() {
             @Override
             public String call() throws Exception {
                 Response response= task.execute();
                 String state=response.body().string();
                 String[] login_state=state.split(",");
                 String r;
                 int code=response.code();
                 if(code!=200)
                     return "请求失败";
                 if(!login_state[0].contains("u"))
                     return "密码错误";
                 else
                     return "密码正确";

             }
         };
            FutureTask<String> futureTask=new FutureTask<>(callable);
            Thread thread=new Thread(futureTask);
            thread.start();
        return futureTask.get();
      }
    //注册程序
    /*
    * 注册程序，返回登录状态字符串
    *1.请输入用户名：account为空
    *2.请输入密码：account不为空，password为空
    *3.连接成功，返回服务器给的注册细节
    * 4.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
    * */
    public String Register() throws JSONException, ExecutionException, InterruptedException {
        if(account.length()==0)
            return "请输入用户名";
        if(password.length()==0)
            return "请输入密码";
        String URL="http://darrendanielday.club/api/Account/MailAuth/Register";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        json_account_message.put("mailAddress",account);
        json_account_message.put("password",password);
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        //同步任务开始
        Callable<String> stringCallable=new Callable<String>() {
            @Override
            public String call() throws Exception {
               Response response=task.execute();
               if(response.code()!=200)
                   return "连接失败";
               String state=response.body().string();
               Gson gson= new Gson();
               Map<String,Object> map=new HashMap<String, Object>();
               map=gson.fromJson(state,map.getClass());
                return (String) map.get("detail");
            }
        };
        FutureTask<String> futureTask=new FutureTask<String>(stringCallable);
        Thread thread=new Thread(futureTask);
        thread.start();
        return futureTask.get();
    }
    /*忘记密码程序:通过用户名来找回密码
    *1.用户没有输入用户名，返回“请输入用户名”
    *2.返回服务器发送的信息
    * 3.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
    * */
    public String Forget_password() throws JSONException, ExecutionException, InterruptedException {
        if(account.length()==0)
            return "请输入用户名";
        String URL="http://darrendanielday.club/api/Account/MailAuth/ForgetPassword";
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        MediaType mediaType=MediaType.parse("application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("mailAddress",account);
        RequestBody requestbody=RequestBody.create(String.valueOf(jsonObject),mediaType);
        Request request=new Request.Builder()
                .post(requestbody)
                .url(URL)
                .build();
        final  Call task=client.newCall(request);
        Callable<String> stringCallable=new Callable<String>() {
            @Override
            public String call() throws Exception {
                 Response response=task.execute();
                 if(response.code()!=200)
                 {
                     //Log.d("Mainactivity","code---------------->"+response.code()+response.body().string());
                     return "连接失败";

                 }
                 String state=response.body().string();
                 Gson gson=new Gson();
                 Map<String,Object> map=new HashMap<String, Object>();
                 map=gson.fromJson(state,map.getClass());
                return (String) map.get("detail");
            }
        };
        FutureTask<String> futureTask=new FutureTask<String>(stringCallable);
        Thread thread=new Thread(futureTask);
        thread.start();
        return futureTask.get();
    }

}

