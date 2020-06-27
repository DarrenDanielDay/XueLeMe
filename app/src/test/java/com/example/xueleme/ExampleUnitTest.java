package com.example.xueleme;

import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.databases.DatabaseAccessor;
import com.example.xueleme.models.locals.ChatMessage;
import com.example.xueleme.models.locals.ChatRecord;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_enum() throws Exception {
        Class<?> clazz = ChatRecord.MessageType.class;
        assertTrue(clazz.isEnum());
        Object[] objects = clazz.getEnumConstants();
        Object object = clazz.getMethod("values").invoke(null);
        assertTrue(object.getClass().isArray());
        for (int i = 0; i < objects.length; i++) {
            System.out.println(((Object[]) objects[i].getClass().getMethod("values").invoke(objects[i]))[i]);
        }
        System.out.println(new DatabaseAccessor<>(ChatMessage.class).createSQL());
    }
}