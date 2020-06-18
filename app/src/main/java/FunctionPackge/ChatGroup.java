package FunctionPackge;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import interface_packge.RequestHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatGroup {
    private Integer groupId;
    private  String groupName;
    private Member owner;
    private List<Member> memberList;
    private String Host="http://darrendanielday.club/";

    public ChatGroup(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        owner=new Member(-1,"","");
        memberList=new ArrayList<Member>();
    }
    public ChatGroup(Integer groupId) {
        this.groupId = groupId;
        owner=new Member(-1,"","");
        memberList=new ArrayList<Member>();
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public void setOwner(Member owner) {
        this.owner = owner;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void SetChatGroup(List<Member> memberList) {
        this.memberList = memberList;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public Member getOwner() {
        return owner;
    }
    public List<Member> getMemberList() {
        return memberList;
    }

    public void GetDetail(final RequestHandler requestHandler)//获取群聊的具体信息，群主群成员，并修改到owner和memberList里
    {
        String URL="api/ChatGroup/Detail";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+groupId)
                .build();
        final Call task=okHttpClient.newCall(request);
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
                    return;
                }
                else
                {
                    Map<String,Object> mapper= (Map<String, Object>) map.get("extraData");
                    Map<String,Object> master= (Map<String, Object>) mapper.get("owner");
                    List<Map<String,Object>> mapList= (List<Map<String, Object>>) mapper.get("members");
                    owner.avatar= (String) master.get("avatar");
                    owner.nickName= (String) master.get("nickname");
                    owner.id= ((Double) master.get("id")).intValue();
                    List<Member> memberList1=new ArrayList<Member>();
                    for(int i=0;i<mapList.size();i++)
                    {
                        Map<String,Object> map1=mapList.get(i);
                        String N= (String) map1.get("nickname");
                        String A= (String) map1.get("avatar");
                        Integer ID= ((Double) map1.get("id")).intValue();
                        Member m=new Member(ID,N,A);
                        memberList1.add(m);
                    }
                    memberList=memberList1;
                    requestHandler.requestSuccess();
                }
            }
        });
    }
}

