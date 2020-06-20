package com.example.xueleme.business;

import com.example.xueleme.models.forms.chatrecord.ChatRecordsBeforeTimeForm;
import com.example.xueleme.models.locals.ChatRecord;

import java.util.List;

public interface IChatRecordController {
    void getChatRecordDetailOfId(UserAction<Integer, ChatRecord, String> action);
    void getChatRecordsBeforeTime(UserAction<ChatRecordsBeforeTimeForm, List<ChatRecord>, String> action);
}
