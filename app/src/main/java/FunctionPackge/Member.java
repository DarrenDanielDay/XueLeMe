package FunctionPackge;

import FunctionPackge.Users;

//群聊成员信息类
public class Member {
    public Integer id;
    public String nickName;
    public String avatar;
    public Member(int id, String nickName, String avatar) {
        this.id = id;
        this.nickName = nickName;
        this.avatar = avatar;
    }
    public Member(Users users) {
        this.id = users.getUserid();
        this.nickName = users.getNick_name();
        this.avatar = users.getAvatar();
    }
    public Member()
    {
        id=-1;
        nickName=null;
        avatar=null;
    }
}
