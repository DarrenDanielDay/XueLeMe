package com.example.xueleme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xueleme.models.DateParser;
import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.databases.DatabaseAccessor;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.ChatRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.xueleme", appContext.getPackageName());
    }
    @Test
    public void testJsonArray() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("aaa");
        jsonObject.put("bbb", jsonArray);
        assertEquals("{\"bbb\":[\"aaa\"]}", jsonObject.toString());
        class A extends ReflectiveJSONModel<A> {
            public List<String> integerList;
        }
        A a = new A();
        a.integerList = new ArrayList<>();
        a.integerList.add("123");
        a.integerList.add("456");
        String aString = a.serialize().toString();
        assertEquals("{\"integerList\":[\"123\",\"456\"]}", aString);
    }

    @Test
    public void testDatabaseAccessor() throws Exception {
        DatabaseAccessor<ChatMessage> chatMessageDatabaseAccessor = new DatabaseAccessor<>(ChatMessage.class);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SQLiteOpenHelper helper = new SQLiteOpenHelper(appContext, chatMessageDatabaseAccessor.tableName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                chatMessageDatabaseAccessor.doCreate(getReadableDatabase());

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id = 1;
        chatMessage.senderName = "ss";
        chatMessage.groupName = "gg";
        chatMessage.groupId = 1;
        chatMessage.senderId = 2;
        chatMessage.createdTime = new Date();
        chatMessage.content = "aaa";
        chatMessage.type = ChatRecord.MessageType.TEXT;
        chatMessages.add(chatMessage);
        SQLiteDatabase database = helper.getReadableDatabase();
        chatMessageDatabaseAccessor.insertAll(chatMessages, database);
        List<ChatMessage> queryResult = chatMessageDatabaseAccessor.fetchAll(database.query(chatMessageDatabaseAccessor.tableName, null, null, null, null, null, null, null));
//        assertEquals(DateParser.format.format(chatMessage.createdTime), DateParser.format.format(queryResult.get(queryResult.size() - 1).createdTime));
        assertEquals("aaa", queryResult.get(queryResult.size() - 1).content);
    }
}
