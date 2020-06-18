package FunctionPackge;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.signalr.Action5;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import interface_packge.ChatHubHandler;
import interface_packge.ConnectionHandler;
import interface_packge.DetailMessage;
import interface_packge.ForepawsHandler;
import interface_packge.GroupCreator;
import interface_packge.IncName;
import interface_packge.JoinGroup;
import interface_packge.LoginHandler;
import interface_packge.PostmethodHandler;
import interface_packge.RegisterHandler;
import interface_packge.ReplyHandler;
import interface_packge.ReplyOfTopicHandler;
import interface_packge.RequestHandler;
import interface_packge.TopicHandler;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
    public LoginHandler loginHandler;
    public RegisterHandler registerHandler;
    public ForepawsHandler forepawsHandler;
    private String nickName;
    private String avatar;//头像的MD5
    public IncName inickName;
    public DetailMessage detailMessage;
    public ConnectionHandler connectionHandler;
    private int userid;
    public ChatHubHandler handler;
    private HubConnection hubConnection;
    private boolean IsConnected;
    private final String URI="http://129.204.245.98:5000/api/ChatRoom";
    private final String Host="http://darrendanielday.club/";
    public GroupCreator groupCreator;
    private List<ZoneMessage> listZone;
    public List<TopicMessage> topicList;
    public JoinGroup joinGroup;
    public Map<Groupkey, Integer> chatGroupMap;//0表示用户是成员，1表示用户是群主
    public Map<Integer, JoinGroupRequest> GroupJoinMessage;//key--value 表示加群信息，key是服务器给的requestID
    //一个加群请求分配唯一的requestID,对应着加群者的用户ID、想加入的群的ID
    //构造函数
    public Users(String account, String password) {
        this.account = account;
        this.password = password;
        chatGroupMap=new HashMap<Groupkey,Integer>();
        GroupJoinMessage=new HashMap<Integer, JoinGroupRequest>();
        IsConnected=false;
        listZone=new ArrayList<ZoneMessage>();
        topicList=new ArrayList<TopicMessage>();
    }

    public List<ZoneMessage> getListZone() {
        return listZone;
    }

    public List<TopicMessage> getTopicList() {
        return topicList;
    }

    public void setHandler(ChatHubHandler handler) {
        this.handler = handler;
    }

    public void setJoinGroup(JoinGroup joinGroup) {
        this.joinGroup = joinGroup;
    }

    public void setGroupCreator(GroupCreator groupCreator) {
        this.groupCreator = groupCreator;
    }

    public Map<Groupkey, Integer> getChatGroupMap() {
        return chatGroupMap;
    }

    public int getUserid() {
        return userid;
    }

    public void setInickName(IncName inickName) {
        this.inickName = inickName;
    }

    public void setDetailMessage(DetailMessage detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setKick_name(String kick_name) {
        this.nickName = kick_name;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }
    public Map<Integer, JoinGroupRequest> getGroupJoinMessage() {
        return GroupJoinMessage;
    }
    public String getAvatar() {
        return avatar;
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
    public void setRegisterHandler(RegisterHandler register_interface1){
        registerHandler =register_interface1;}
    public void setForepawsHandler(ForepawsHandler forepawsHandler1){
        forepawsHandler = forepawsHandler1;}
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
        {loginHandler.accountIsnull();
            return;
        }
        if (password.length() == 0)
        {loginHandler.passwordIsnull();
            return;
        }
        String URL = "api/Account/MailAuth/Login";
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
            loginHandler.connectionFailed();
            return;
        }
        //创建请求体
        RequestBody login_message = RequestBody.create(String.valueOf(json_account_message), mediaType);
        //创建请求
        Request login_request = new Request.Builder()
                .post(login_message)
                .url(Host+URL)
                .build();
        //创建任务并异步传输
        final Call task = client.newCall(login_request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                loginHandler.connectionFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state= response.body().string();
                String[] login_state=state.split(",");
                String r;
                int code=response.code();
                if(code!=200)
                    loginHandler.connectionFailed();
                if(!login_state[0].contains("u"))
                    loginHandler.passwordWrong();
                else
                    loginHandler.passwordCorrect();
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
        {
            registerHandler.accountIsnull();
            return;
        }
        if(password.length()==0){
            registerHandler.passwordIsnull();
            return;}
        String URL="api/Account/MailAuth/Register";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("mailAddress",account);
            json_account_message.put("password",password);}catch (JSONException e){
            registerHandler.JSONError();}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                registerHandler.requestFailed();
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
                    registerHandler.successRegister();
                }
                else if(s.equals("邮箱已注册"))
                {
                    registerHandler.alreadyRegister();
                }
                else
                {
                    forepawsHandler.requestFailed();
                }
            }
        });
        //同步任务开始
       /* Callable<String> stringCallable=new Callable<String>() {
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
        return futureTask.get();*/
    }
    /*忘记密码程序:通过用户名来找回密码
     *1.用户没有输入用户名，返回“请输入用户名”
     *2.返回服务器发送的信息,邮箱未注册、请求成功
     * 3.网络不佳，请求失败,此时会抛出异常，因为异常在函数体内无法添加，所以还请前端自行添加异常时的操作
     * */
    public void Forget_password(){
        if(account.length()==0)
        {
            forepawsHandler.accountIsnull();
            return;
        }

        String URL="api/Account/MailAuth/ForgetPassword";
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        MediaType mediaType=MediaType.parse("application/json");
        JSONObject jsonObject=new JSONObject();
        try{ jsonObject.put("mailAddress",account);}catch (JSONException e){
            forepawsHandler.requestFailed();}
        RequestBody requestbody=RequestBody.create(String.valueOf(jsonObject),mediaType);
        Request request=new Request.Builder()
                .post(requestbody)
                .url(Host+URL)
                .build();
        final  Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                forepawsHandler.requestFailed();
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
                    forepawsHandler.accountNonexistence();
                }
                else if(s.equals("请求成功"))
                {
                    forepawsHandler.requestSuccess();
                }
                else
                {
                    forepawsHandler.requestFailed();
                }
            }
        });
    }
    /*
     * 查询用户的UserID,并改写User类中的userid
     * 这项操作应该是在用户登录成功时执行的
     * 主要功能就是从服务器获取userid,保存到User类中，以便以后使用
     * */
    public void IDquery()
    {
        String URL="api/Account/MailAuth/QueryId";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"?mail="+account)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                connectionHandler.connectionFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state=response.body().string();
                String[] message=state.split(",");
                String id_me=message[0];
                String detail_me=message[2];
                String[] s1=id_me.split(":");
                String[] s2=detail_me.split(":");
                if(s1.length!=2||s2.length!=2)
                {
                    connectionHandler.connectionFailed();
                    return ;
                }
                if(response.code()!=200)
                {
                    connectionHandler.connectionFailed();
                    return;
                }
                String id=s1[1];
                String detail=new String();
                int i=1;
                int length=s2[1].length()-3;
                while(i<=length)
                {
                    detail=detail+(s2[1]).charAt(i);
                    i++;
                }
                if(detail.equals("查询成功"))
                {
                    userid=Integer.parseInt(id);
                    connectionHandler.connectionSuccess();
                }
                else
                {
                    connectionHandler.connectionFailed();
                }
            }
        });
    }
    /*
     * 修改昵称API
     * */
    public void Change_Nickname(final String nickname)
    {
        if(nickname.length()==0)
        {
            inickName.nickNumskull();
            return;
        }
        String URL="api/Account/ChangeNickname";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("nickname",nickname);}catch (JSONException e){inickName.JSONERROR();; return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                inickName.connectionFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String state=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(state,map.getClass());
                String s= (String) map.get("detail");
                if(s.equals("修改昵称成功"))
                {
                    nickName =nickname;
                    inickName.connectionSuccess();
                    return;
                }
                else
                {
                    inickName.connectionFailed();
                    return;
                }
            }
        });
    }
    /*
     * 获取用户的具体信息，并将其修改到user中去，修改的是头像MD5和昵称，不过需要发送给服务器id，建议在登录的时候获取userid后使用
     * */
    public void getdetails() {
        String URL="api/Account/Detail";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+userid)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                detailMessage.connectionFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String message=response.body().string();
                Gson gson=new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(message,map.getClass());
                Map mapp= (Map) map.get("extraData");
                Double state= (Double) map.get("state");
                if(state==3)
                {
                    detailMessage.NoAccount();
                    return;
                }
                else
                {
                    nickName = (String) mapp.get("nickname");
                    avatar= (String) mapp.get("avatar");
                }
                detailMessage.connectionSuccess();
            }
        });
    }
    /*
     * 用户创建群聊，并在自己的chatgroupmap里添加群聊信息
     * */
    public void CreatGroup(final String groupName)
    {
        if(groupName.length()==0)
        {
            groupCreator.groupNameIsNull();
            return;
        }
        String URL="api/ChatGroup/Create";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("groupName",groupName);}catch (JSONException e){groupCreator.jsonError();; return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                groupCreator.requestFailed();
                return ;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state= (Double) map.get("state");
                if(state!=0)
                {
                    groupCreator.requestFailed();
                    return;
                }
                else
                {;
                    Map map1= (Map) map.get("extraData");
                    Integer id= ((Double) map1.get("id")).intValue();
                    Groupkey groupkey=new Groupkey(groupName, id);
                    chatGroupMap.put(groupkey,1);
                    groupCreator.requestSuccess();
                }
            }
        });
    }
    /**
     * 加群申请
     * */
    public void JoinGroupRequest(Integer GroupID)
    {
        Map map=this.getChatGroupMap();
        Iterator<Map.Entry<Groupkey,Integer>> iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Groupkey,Integer> entry = iter.next();
            Groupkey key = entry.getKey();
            Integer value = entry.getValue();
            if(key.groupId==GroupID)
            {
                joinGroup.AlreadyInGroup();
                return;
            }
        }
        String URL="api/ChatGroup/Join";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("groupId",GroupID);}catch (JSONException e){joinGroup.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                joinGroup.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state= (Double) map.get("state");
                if(state==3)
                {
                    joinGroup.GroupNotExist();
                    return;
                }
                else if(state==2)
                {
                    joinGroup.HasRequested();
                    return;
                }
                else if(state==0)
                {
                    joinGroup.requestSuccess();
                    return;
                }
                else
                {
                    joinGroup.requestFailed();
                }
            }
        });
    }
    /*
     * 用户给出群聊ID、群聊名称、和接口任务，修改群聊名称
     * */
    public void ChangeGroupName(Integer GroupID, String name, final PostmethodHandler postmethodHandler)
    {   if(name.length()==0)
    {
        postmethodHandler.ISNULL();
        return;
    }
        String URL="api/ChatGroup/ChangeName";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("groupId",GroupID);
            json_account_message.put("newName",name);}catch (JSONException e){
            postmethodHandler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=0)
                {
                    postmethodHandler.postfailed();
                    return;
                }
                else
                {
                    postmethodHandler.postsuccess();
                    return;
                }
            }
        });
    }
    /*
     * 查询自己作为成员加入的群，并根据结果修改ChatGroupMap
     * */
    public void MyJoinedGroup(final RequestHandler requestHandler)
    {
        String URL="api/ChatGroup/MyJoinedGroup";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+userid)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestHandler.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                List<Map> mapList= new ArrayList<>();
                mapList= (List<Map>) map.get("extraData");
                Double state= (Double) map.get("state");
                if(state!=2)
                {
                    requestHandler.requestFailed();
                    return;
                }
                else
                {
                    for (int i=0;i<mapList.size();i++)
                    {
                        Map mapp=mapList.get(i);
                        Double id= (Double) mapp.get("id");
                        Integer groupid=id.intValue();
                        String name= (String) mapp.get("name");
                        Groupkey groupkey=new Groupkey(name,groupid);
                        Map mapper=chatGroupMap;
                        int tag=0;
                        Iterator<Map.Entry<Groupkey,Integer>> iter = mapper.entrySet().iterator();
                        while(iter.hasNext()){
                            Map.Entry<Groupkey,Integer> entry = iter.next();
                            Groupkey key = entry.getKey();
                            Integer value = entry.getValue();
                            if(key.groupId==groupid)
                                tag=1;
                        }
                        if(tag==0)
                        {chatGroupMap.put(groupkey,0);}
                    }
                    requestHandler.requestSuccess();
                    return;
                }
            }
        });
    }
    /*
     * 查询自己作为群主创建的群，并根据结果修改ChatGroupMap
     * */
    public void MyCreateGroup(final RequestHandler requestHandler)
    {
        String URL="api/ChatGroup/MyCreatedGroup";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+userid)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestHandler.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                List<Map> mapList= new ArrayList<>();
                mapList= (List<Map>) map.get("extraData");
                Double state= (Double) map.get("state");
                if(state!=2)
                {
                    requestHandler.requestFailed();
                    return;
                }
                else
                {
                    for (int i=0;i<mapList.size();i++)
                    {
                        Map mapp=mapList.get(i);
                        Double id= (Double) mapp.get("id");
                        Integer groupid=id.intValue();
                        String name= (String) mapp.get("name");
                        Groupkey groupkey=new Groupkey(name,groupid);
                        Map mapper=chatGroupMap;
                        int tag=0;
                        Iterator<Map.Entry<Groupkey,Integer>> iter = mapper.entrySet().iterator();
                        while(iter.hasNext()){
                            Map.Entry<Groupkey,Integer> entry = iter.next();
                            Groupkey key = entry.getKey();
                            Integer value = entry.getValue();
                            if(key.groupId==groupid)
                                tag=1;
                        }
                        if(tag==0)
                        {chatGroupMap.put(groupkey,1);}
                    }
                    requestHandler.requestSuccess();
                    return;
                }
            }
        });
    }
    /*
     *退出群聊,群主不能退群,并改变chatGroupMap的值
     * */
    public void QuitGroup(final Integer GroupID, final PostmethodHandler postmethodHandler)
    {
        String URL="api/ChatGroup/Quit";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("groupId",GroupID);}catch (JSONException e){
            postmethodHandler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state= (Double) map.get("state");
                if(state!=0)
                {
                    postmethodHandler.postfailed();
                }
                else
                {
                    Iterator<Map.Entry<Groupkey,Integer>> iter = chatGroupMap.entrySet().iterator();
                    while(iter.hasNext()){
                        Map.Entry<Groupkey,Integer> entry = iter.next();
                        Groupkey key = entry.getKey();
                        Integer value = entry.getValue();
                        if(value==0)
                        {
                            if(key.groupId.equals(GroupID))
                            {
                                iter.remove();
                                break;
                            }
                        }
                    }
                    postmethodHandler.postsuccess();
                }
            }
        });
    }
    /*
     *获得加群请求，这个方法也是建议轮询的，即隔一段时间运行一下//之前可以使用
     * */
    public void GetJoinRequest(Integer GroupID, final RequestHandler requestHandler)
    {
        String URL="api/ChatGroup/JoinRequests";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+GroupID)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestHandler.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state= (Double) map.get("state");
                if(state!=2)
                {
                    requestHandler.requestFailed();
                    return ;
                }
                else
                {
                    List<Map<String,Object>> mapList= (List<Map<String, Object>>) map.get("extraData");
                    //遍历这个LIST,判断在不在GroupJoinMessage里面
                    Integer i=0;
                    while(i<mapList.size())
                    {
                        Map<String, Object> mapper=mapList.get(i);
                        Double k1= (Double) mapper.get("id");
                        Double k2= (Double) mapper.get("userId");
                        Double k3= (Double) mapper.get("groupId");
                        Integer requestId=k1.intValue();
                        Integer userId=k2.intValue();
                        Integer groupId=k3.intValue();
                        if(!GroupJoinMessage.containsKey(requestId))
                        {
                            JoinGroupRequest joinGroupRequest=new JoinGroupRequest(userId,groupId);
                            GroupJoinMessage.put(requestId,joinGroupRequest);
                        }
                        i++;
                    }
                    requestHandler.requestSuccess();
                }
            }
        });
    }
    /**
     * 同意加群请求//未验证
     */
    public void AgreeJoin(final Integer requestId, final PostmethodHandler postmethodHandler)
    {
        String URL="api/ChatGroup/AgreeJoin";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("requestId",requestId);}catch (JSONException e){
            postmethodHandler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=0)
                {
                    postmethodHandler.postfailed();
                }
                else
                {   Map mapper=GroupJoinMessage;
                    Iterator<Map.Entry<Integer,JoinGroupRequest>> iter = mapper.entrySet().iterator();
                    while(iter.hasNext()){
                        Map.Entry<Integer,JoinGroupRequest> entry = iter.next();
                        if(entry.getKey().equals(requestId))
                        {
                            mapper.remove(iter);
                            break;
                        }
                    }
                    GroupJoinMessage=mapper;
                    postmethodHandler.postsuccess();
                }
            }
        });
    }
    /**
     * 同意加群请求//未验证
     */
    public void RejectJoin(final Integer requestId, final PostmethodHandler postmethodHandler)
    {
        String URL="api/ChatGroup/RejectJoin";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("userId",userid);
            json_account_message.put("requestId",requestId);}catch (JSONException e){
            postmethodHandler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=0)
                {
                    postmethodHandler.postfailed();
                }
                else
                {   Map mapper=GroupJoinMessage;
                    Iterator<Map.Entry<Integer,JoinGroupRequest>> iter = mapper.entrySet().iterator();
                    while(iter.hasNext()){
                        Map.Entry<Integer,JoinGroupRequest> entry = iter.next();
                        if(entry.getKey().equals(requestId))
                        {
                            mapper.remove(iter);
                            break;
                        }
                    }
                    GroupJoinMessage=mapper;
                    postmethodHandler.postsuccess();
                }
            }
        });
    }
    /*
     * 踢人
     *待验证
     * */
    public void kick(Integer groupId, Integer userId, final PostmethodHandler postmethodHandler)
    {

        String URL="api/ChatGroup/Kick";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("ownerId",userid);
            json_account_message.put("userId",userId);
            json_account_message.put("groupId",groupId);}catch (JSONException e){
            postmethodHandler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postsuccess();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=0)
                {
                    postmethodHandler.postfailed();
                }
                else {
                    postmethodHandler.postsuccess();
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------
    //实时通信部分
    //通信流程：先建立连接，然后加入ROOM，之后就可以发送和接受了
    //--------------------------------------------------------------------------------------
    /*
     * 建立连接
     * */
    public void connect() {
        if (IsConnected) {
            handler.onConnected();//表示连接成功
            return;
        }
        final Users that = this;
        hubConnection = HubConnectionBuilder.create(URI).build();
        hubConnection.onClosed(new OnClosedCallback() {
            @Override
            public void invoke(Exception exception) {
                that.IsConnected = false;
                that.handler.onDisconnected();//失去连接
            }
        });

        // 为接收消息添加监听器, 接收五个参数, 依次是发送方ID, 群聊ID, 消息类型枚举, 字符串内容(如果是图片则是一个md5字符串), 发送时间
        hubConnection.on("OnReceiveMessage", new Action5<Integer, Integer, Integer, String, Date>() {
            @Override
            public void invoke(Integer param1, Integer param2, Integer param3, String param4, Date param5) {
                ChatMessage message = new ChatMessage();
                message.senderId = param1;
                message.groupId = param2;
                message.type = param3;
                message.content = param4;
                message.createdTime = param5;
                that.handler.onReceiveMessage(message);
            }
        }, Integer.class, Integer.class, Integer.class, String.class, Date.class);
        final Completable completable = hubConnection.start().doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                that.IsConnected= true;
                that.joinRoom();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handler.onConnectionFailed(throwable);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 由于此方法是阻塞的，因此最好另开一个线程做
                    completable.blockingAwait();
                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
                }
            }
        }).start();
    }
    //用户加入聊天室
    public void joinRoom() {
        final Users that = this;
        if(!IsConnected)
        {
            handler.onNoConnection();//无连接
            return;
        }
        Consumer<String> consumer=new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                that.handler.onJoinRoomResult(s);
            }
        };
        hubConnection.invoke(String.class,"JoinRoom",userid).subscribe(consumer);
    }
    //用户发送消息
    public void sendMessage(ChatMessage chatMessage)
    {
        final Users that = this;
        if(!IsConnected)
        {
            handler.onNoConnection();//无连接
            return;
        }
        Consumer<String> consumer=new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                that.handler.onSendResult(s);
            }
        };
        hubConnection.invoke(String.class, "SendMessage", chatMessage.groupId, chatMessage.type, chatMessage.content).subscribe(consumer);
    }
    //断开集线器的连接
    public void disconnect() {
        if (IsConnected) {
            hubConnection.stop();
            IsConnected = false;
        }
    }
    /*
     * -----------------------------------------------------------
     * 论坛部分
     * ------------------------------------------------------------
     * */
    //创建论坛
    public void CreateZone(String zoneName, final PostmethodHandler handler)
    {
        if(zoneName.length()<=0)
        {
            handler.ISNULL();
        }
        String URL="http://darrendanielday.club/api/Topic/CreateZone";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        try{json_account_message.put("zoneName",zoneName);
        }catch (JSONException e){handler.JSONERROR(); return;}
        RequestBody requestBody=RequestBody.create(String.valueOf(json_account_message),mediaType);
        Request request=new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=0||response.code()!=200)
                {
                    handler.postfailed();
                }
                else
                {
                    handler.postsuccess();
                }
            }
        });
    }
    //创建话题
    public void CreateTopic(Topic topic, final PostmethodHandler handler)
    {
        topic.userId=userid;
        String URL="api/Topic/CreateTopic";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        Map<String,Object> mapp=new HashMap<String, Object>();
        mapp.put("title",topic.title);
        mapp.put("userId",topic.userId);
        mapp.put("zoneId",topic.zoneId);
        mapp.put("tags",topic.tags);
        mapp.put("content",topic.content);
        mapp.put("images",topic.images);
        Gson gson1=new Gson();
        String r=gson1.toJson(mapp);
        RequestBody requestBody=RequestBody.create(r,mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(response.code()!=200||state!=0)
                {
                    handler.postfailed();
                    return;
                }
                else
                {
                    handler.postsuccess();
                    return;
                }
            }
        });
    }
    //获得所有的分区,并存放到ListZone里
    public void GetAllZone(final RequestHandler requestHandler)
    {
        String URL="api/Topic/AllZones";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestHandler.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(state!=2||response.code()!=200)
                {
                    requestHandler.requestFailed();
                    return;
                }
                else
                {
                    List<Map<String,Object>> mapList= (List<Map<String, Object>>) map.get("extraData");
                    List<ZoneMessage> zoneMessageList=new ArrayList<ZoneMessage>();
                    for(int i=0;i<mapList.size();i++)
                    {
                        Map k=mapList.get(i);
                        Integer id=((Double)k.get("id")).intValue();
                        String zoneName= (String) k.get("zoneName");
                        ZoneMessage zoneMessage=new ZoneMessage();
                        zoneMessage.zoneId=id;
                        zoneMessage.zoneName=zoneName;
                        zoneMessageList.add(zoneMessage);
                    }
                    listZone=zoneMessageList;
                    requestHandler.requestSuccess();
                    return;
                }
            }
        });
    }
    //获得某个分区的所有话题
    public void GetAllTopic(Integer ZoneID, final RequestHandler requestHandler)
    {
        String URL="api/Topic/AllTopics";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+ZoneID)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requestHandler.requestFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");

                if(state!=2||response.code()!=200)
                {
                    requestHandler.requestFailed();
                    return;
                }
                else
                {   List<TopicMessage> topicMessageList=new ArrayList<TopicMessage>();
                    List<Map<String,Object>> mapList= (List<Map<String, Object>>) map.get("extraData");
                    for(int i=0;i<mapList.size();i++)
                    {
                        Map map1=mapList.get(i);
                        TopicMessage topicMessage=new TopicMessage();
                        topicMessage.topicid= ((Double) map1.get("id")).intValue();
                        topicMessage.title= (String) map1.get("title");
                        Map Publisher= (Map) map1.get("publisherDetail");
                        topicMessage.publisherId= ((Double) Publisher.get("userId")).intValue();
                        topicMessage.publisherName= (String) Publisher.get("fakename");
                        topicMessage.zoneId=((Double)map1.get("zoneId")).intValue();
                        topicMessage.tags= (List<String>) map1.get("tags");
                        Map map2= (Map) map1.get("contentDetail");
                        topicMessage.contentId=((Double)map2.get("id")).intValue();
                        topicMessage.contentext= (String) map2.get("text");
                        topicMessage.images= (List<String>) map2.get("images");
                        topicMessageList.add(topicMessage);
                    }
                    topicList=topicMessageList;
                    requestHandler.requestSuccess();
                    return;
                }
            }
        });
    }
    //回复*****
    public void MakeReply(ReplyMessage replyMessage, final PostmethodHandler postmethodHandler)
    {
        replyMessage.userId=userid;
        String URL="api/Topic/MakeReply";
        MediaType mediaType=MediaType.parse("application/json");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        JSONObject json_account_message=new JSONObject();
        Map<String,Object> mapp=new HashMap<String, Object>();
        mapp.put("userId",replyMessage.userId);
        mapp.put("topicId",replyMessage.topicId);
        mapp.put("referenceId",replyMessage.referenceId);
        mapp.put("myProperty",replyMessage.myProperty);
        mapp.put("content",replyMessage.content);
        mapp.put("images",replyMessage.images);
        Gson gson1=new Gson();
        String r=gson1.toJson(mapp);
        RequestBody requestBody=RequestBody.create(r,mediaType);
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                postmethodHandler.postfailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(response.code()!=200||state!=0)
                {
                    postmethodHandler.postfailed();
                    return;
                }
                else
                {
                    postmethodHandler.postsuccess();
                    return;
                }
            }
        });
    }
    //返回一个topic的细节信息
    public void GetTopicDetail(Integer topicId, final TopicHandler topicHandler)
    {
        String URL="api/Topic/TopicDetail";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+topicId)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                topicHandler.GetTopicFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(response.code()!=200||state!=0)
                {
                    topicHandler.GetTopicFailed();
                    return;
                }
                else
                {
                    TopicMessage topicMessage=new TopicMessage();
                    Map map1= (Map) map.get("extraData");
                    topicMessage.topicid= ((Double) map1.get("id")).intValue();
                    topicMessage.title= (String) map1.get("title");
                    Map Publisher= (Map) map1.get("publisherDetail");
                    topicMessage.publisherId= ((Double) Publisher.get("userId")).intValue();
                    topicMessage.publisherName= (String) Publisher.get("fakename");
                    topicMessage.zoneId=((Double)map1.get("zoneId")).intValue();
                    topicMessage.tags= (List<String>) map1.get("tags");
                    Map map2= (Map) map1.get("contentDetail");
                    topicMessage.contentId=((Double)map2.get("id")).intValue();
                    topicMessage.contentext= (String) map2.get("text");
                    topicMessage.images= (List<String>) map2.get("images");
                    topicHandler.GetTopicSuccess(topicMessage);
                    return;
                }
            }
        });
    }
    //返回一个reply的细节信息
    public void GetReplyDetail(Integer replyid, final ReplyHandler replyHandler)
    {
        String URL="api/Topic/ReplyDetail";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+replyid)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                replyHandler.GetReplyFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map mapp=new HashMap<String, Object>();
                mapp=gson.fromJson(s,mapp.getClass());
                Double state=(Double)mapp.get("state");
                if(response.code()!=200||state!=0)
                {
                    replyHandler.GetReplyFailed();
                    return;
                }
                else
                {   Map map= (Map) mapp.get("extraData");
                    ReplyDetails replyDetails=new ReplyDetails();
                    replyDetails.replyid= ((Double) map.get("id")).intValue();
                    replyDetails.topicId= ((Double) map.get("topicId")).intValue();
                    Map map1= (Map) map.get("user");
                    replyDetails.userId=((Double)map1.get("userId")).intValue();
                    replyDetails.fakename= (String) map1.get("fakename");
                    Map map2= (Map) map.get("contentDetail");
                    replyDetails.contentId=((Double)map2.get("id")).intValue();
                    replyDetails.contentext= (String) map2.get("text");
                    replyDetails.images= (List<String>) map2.get("images");
                    replyHandler.GetReplySuccessed(replyDetails);
                    return;
                }
            }
        });
    }
    //返回一个Topic里面的所有reply
    public void GetReplyOfTopic(Integer topicId, final ReplyOfTopicHandler replyOfTopicHandler)
    {
        String URL="api/Topic/RepliesOfTopic";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000,TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+topicId)
                .build();
        final  Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                replyOfTopicHandler.GetReplyOfTopicFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map mapp=new HashMap<String, Object>();
                mapp=gson.fromJson(s,mapp.getClass());
                Double state=(Double)mapp.get("state");
                if(response.code()!=200||state!=2)
                {
                    replyOfTopicHandler.GetReplyOfTopicFailed();
                    return;
                }
                else
                {
                    List<Map<String,Object>> mapList= (List<Map<String, Object>>) mapp.get("extraData");
                    List<ReplyDetails> replyDetails=new ArrayList<ReplyDetails>();
                    for(int i=0;i<mapList.size();i++)
                    {
                        Map map=mapList.get(i);
                        ReplyDetails replyDetails1=new ReplyDetails();
                        replyDetails1.replyid=((Double)map.get("id")).intValue();
                        replyDetails1.topicId=((Double)map.get("topicId")).intValue();
                        Map map1= (Map) map.get("user");
                        replyDetails1.userId=((Double)map1.get("userId")).intValue();
                        replyDetails1.fakename= (String) map1.get("fakename");
                        Map map2= (Map) map.get("contentDetail");
                        replyDetails1.contentId=((Double)map2.get("id")).intValue();
                        replyDetails1.contentext= (String) map2.get("text");
                        replyDetails1.images= (List<String>) map2.get("images");
                        replyDetails.add(replyDetails1);
                    }
                    replyOfTopicHandler.GetReplyOfTopicSucess(replyDetails);
                }
            }
        });
    }
}