package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DB_Tools extends SQLiteOpenHelper {
    public DB_Tools(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个日程表,
        // 有6个字段： data表示日期，格式是年-月-日，
        // task表示任务，
        // state表示完成状态，0表示未完成未逾期，1表示已完成，-1表示已逾期，
        // 还有结束时间、开始时间，结束时间和开始时间格式是小时-分钟
        String sql="create table calendar(data varchar(100),task varchar(100),start_time varchar(100),end_time varchar(100),primary key(data,task))";
        db.execSQL(sql);
        Log.d("database_create:","数据库创建......");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("database_upgrade:","数据库更新......");
    }
}
