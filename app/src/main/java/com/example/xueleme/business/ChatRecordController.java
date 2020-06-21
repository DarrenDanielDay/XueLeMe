package com.example.xueleme.business;

import com.example.xueleme.models.DateParser;
import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.forms.chatrecord.ChatRecordsBeforeTimeForm;
import com.example.xueleme.models.locals.ChatRecord;
import com.example.xueleme.models.responses.ChatRecordDetail;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRecordController extends RequestController implements IChatRecordController {
    @Override
    public void getChatRecordDetailOfId(UserAction<Integer, ChatRecord, String> action) {
        handleGetAction(action, "api/ChatRecord/Detail/" + action.data, ServiceResultEnum.EXIST, ServiceResult.ofGeneric(ChatRecordDetail.class), new ResponseModelAdapter<ChatRecordDetail, ChatRecord>() {
            @Override
            public ChatRecord convert(ChatRecordDetail chatRecordDetail) {
                return ChatRecord.fromDetail(chatRecordDetail);
            }
        });
    }

    @Override
    public void getChatRecordsBeforeTime(UserAction<ChatRecordsBeforeTimeForm, List<ChatRecord>, String> action) {
        Date date = action.data.time;
        String dateString = DateParser.format.format(date);
        handleGetAction(action, "api/ChatRecord/RecordsBeforeTime?groupId=" + action.data.groupId + "&time=" + dateString + "&limit=" + action.data.limit, ServiceResultEnum.EXIST, ServiceResult.listParser(ChatRecordDetail.class), new ResponseModelAdapter<List<ChatRecordDetail>, List<ChatRecord>>() {
            @Override
            public List<ChatRecord> convert(List<ChatRecordDetail> chatRecordDetails) {
                List<ChatRecord> records = new ArrayList<>();
                for (ChatRecordDetail chatRecordDetail: chatRecordDetails) {
                    records.add(ChatRecord.fromDetail(chatRecordDetail));
                }
                return records;
            }
        });
    }
}
