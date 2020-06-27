package com.example.xueleme;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.xueleme.models.ReflectiveJSONModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
}
