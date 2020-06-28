package com.example.xueleme.models.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xueleme.models.locals.ChatMessage;

import java.lang.reflect.Field;

public class MyEntityDatabaseHelper extends SQLiteOpenHelper {
    public final DatabaseAccessor<ChatMessage> chatMessageDatabaseAccessor = new DatabaseAccessor<>(ChatMessage.class);
    public static final String DATABASE_FILE = "MyDatabase.db";
    public MyEntityDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Field field: this.getClass().getFields()) {
            if (field.getType().equals(DatabaseAccessor.class)) {
                try {
                    DatabaseAccessor databaseAccessor = (DatabaseAccessor) field.get(this);
                    databaseAccessor.doCreate(db);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
