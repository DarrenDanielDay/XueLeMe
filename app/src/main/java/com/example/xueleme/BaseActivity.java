package com.example.xueleme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Stack;

public class BaseActivity extends AppCompatActivity {
    public static final Collection<BaseActivity> activities = new HashSet<>();
    public static final Stack<BaseActivity> activityStack = new Stack<>();
    public static final String SIGNAL_FORCE_OUT = "com.example.xueleme.FORCE_OUT";
    private BroadcastReceiver receiver;
    public static void finishAll() {
        Log.d(BaseActivity.class.getSimpleName(), "正在结束所有活动...");
        for (BaseActivity activity: activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityStack.push(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseActivity.finishAll();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(SIGNAL_FORCE_OUT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        activityStack.pop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }
}
