package com.example.xueleme.business;

import android.util.Log;

import com.example.xueleme.models.ResponseModelAdapter;
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
import com.example.xueleme.models.responses.GroupDetail;
import com.example.xueleme.models.responses.JoinGroupRequestBrief;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupController extends RequestController implements IChatGroupController {
    @Override
    public void createGroup(UserAction<CreateGroupForm, ChatGroup, String> action) {
        handlePostAction(action, "api/ChatGroup/Create", ServiceResultEnum.SUCCESS, ServiceResult.ofGeneric(GroupDetail.class), new ResponseModelAdapter<GroupDetail, ChatGroup>() {
            @Override
            public ChatGroup convert(GroupDetail groupDetail) {
                return ChatGroup.fromDetail(groupDetail);
            }
        });
    }

    @Override
    public void changeGroupName(UserAction<ChangeGroupNameForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/ChangeName", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void joinGroup(UserAction<JoinGroupForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/Join", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void getJoinGroupRequests(UserAction<ChatGroup, List<JoinGroupRequest>, String> action) {
        handleGetAction(action, "api/ChatGroup/JJoinRequests/" + action.data.id, ServiceResultEnum.EXIST, ServiceResult.listParser(JoinGroupRequestBrief.class), new ResponseModelAdapter<List<JoinGroupRequestBrief>, List<JoinGroupRequest>>() {
            @Override
            public List<JoinGroupRequest> convert(List<JoinGroupRequestBrief> joinGroupRequestBriefs) {
                return null;
            }
        });
    }

    @Override
    public void agreeJoin(UserAction<AgreeJoinGroupForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/AgreeJoin", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void rejectJoin(UserAction<RejectJoinForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/RejectJoin", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void quitGroup(UserAction<QuitGroupForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/Quit", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void kickUser(UserAction<KickUserForm, String, String> action) {
        handlePostAction(action, "api/ChatGroup/Kick", ServiceResultEnum.SUCCESS);
    }

    @Override
    public void getChatGroupDetail(UserAction<Integer, ChatGroup, String> action) {
        handleGetAction(action, "api/ChatGroup/Detail/" + action.data, ServiceResultEnum.EXIST, ServiceResult.ofGeneric(GroupDetail.class), new ResponseModelAdapter<GroupDetail, ChatGroup>() {
            @Override
            public ChatGroup convert(GroupDetail groupDetail) {
                return ChatGroup.fromDetail(groupDetail);
            }
        });
    }

    @Override
    public void getMyJoinedGroupList(UserAction<User, List<ChatGroup>, String> action) {
        handleGetAction(action, "api/ChatGroup/MyJoinedGroup/" + action.data.id, ServiceResultEnum.EXIST, ServiceResult.listParser(BriefGroup.class), new ResponseModelAdapter<List<BriefGroup>, List<ChatGroup>>() {
            @Override
            public List<ChatGroup> convert(List<BriefGroup> briefGroups) {
                List<ChatGroup> chatGroups = new ArrayList<>();
                for (BriefGroup briefGroup: briefGroups) {
                    getChatGroupDetail(new UserAction<>(briefGroup.id, new ActionResultHandler<ChatGroup, String>() {
                        @Override
                        public void onSuccess(ChatGroup chatGroup) {
                            chatGroups.add(chatGroup);
                        }

                        @Override
                        public void onError(String s) {
                            Log.d("getMyJoinedGroupList", s);
                        }
                    }));
                }
                while (chatGroups.size() != briefGroups.size());
                return chatGroups;
            }
        });
    }

    @Override
    public void getMyCreatedGroupList(UserAction<User, List<ChatGroup>, String> action) {
        handleGetAction(action, "api/ChatGroup/MyCreatedGroup/" + action.data.id, ServiceResultEnum.EXIST, ServiceResult.listParser(BriefGroup.class), new ResponseModelAdapter<List<BriefGroup>, List<ChatGroup>>() {
            @Override
            public List<ChatGroup> convert(List<BriefGroup> briefGroups) {
                List<ChatGroup> chatGroups = new ArrayList<>();
                for (BriefGroup briefGroup: briefGroups) {
                    getChatGroupDetail(new UserAction<>(briefGroup.id, new ActionResultHandler<ChatGroup, String>() {
                        @Override
                        public void onSuccess(ChatGroup chatGroup) {
                            chatGroups.add(chatGroup);
                        }

                        @Override
                        public void onError(String s) {
                            Log.d("getMyCreatedGroupList", s);
                        }
                    }));
                }
                while (chatGroups.size() != briefGroups.size());
                return chatGroups;
            }
        });
    }
}
