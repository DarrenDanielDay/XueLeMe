package com.example.xueleme;

import java.util.List;

public class ChatGroup {
    private Integer groupId;
    private  String groupName;
    private Member owner;
    private List<Member> memberList;
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public void setOwner(Member owner) {
        this.owner = owner;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public ChatGroup(List<Member> memberList) {
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

}
