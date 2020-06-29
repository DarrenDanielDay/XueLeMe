package com.example.xueleme.business;

import com.example.xueleme.models.forms.chatgroup.AgreeJoinGroupForm;
import com.example.xueleme.models.forms.chatgroup.ChangeGroupNameForm;
import com.example.xueleme.models.forms.chatgroup.CreateGroupForm;
import com.example.xueleme.models.forms.chatgroup.JoinGroupForm;
import com.example.xueleme.models.forms.chatgroup.KickUserForm;
import com.example.xueleme.models.forms.chatgroup.QuitGroupForm;
import com.example.xueleme.models.forms.chatgroup.RejectJoinForm;
import com.example.xueleme.models.locals.ChatGroup;
import com.example.xueleme.models.locals.JoinGroupRequest;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.BriefGroup;

import java.util.List;

public interface IChatGroupController {
    void createGroup(UserAction<CreateGroupForm, ChatGroup, String> action);
    void changeGroupName(UserAction<ChangeGroupNameForm, String, String> action);
    void joinGroup(UserAction<JoinGroupForm, String, String> action);
    void searchGroupsByName(UserAction<String, List<BriefGroup>, String> action);
    void getJoinGroupRequests(UserAction<ChatGroup, List<JoinGroupRequest>, String> action);
    void agreeJoin(UserAction<AgreeJoinGroupForm, String, String> action);
    void rejectJoin(UserAction<RejectJoinForm, String, String> action);
    void quitGroup(UserAction<QuitGroupForm, String, String> action);
    void kickUser(UserAction<KickUserForm, String, String> action);
    void getChatGroupDetail(UserAction<Integer, ChatGroup, String> action);
    void getMyJoinedGroupList(UserAction<User, List<ChatGroup>, String> action);
    void getMyCreatedGroupList(UserAction<User, List<ChatGroup>, String> action);
}
