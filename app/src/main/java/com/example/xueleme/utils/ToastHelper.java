package com.example.xueleme.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastHelper {
    public static void showToastWithLooper(Context context, String message) {
        Looper.prepare();
        showToast(context, message);
        Looper.loop();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
