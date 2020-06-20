package com.example.xueleme.business;

import android.util.Log;

public class DefaultHandler<TResult, TError> implements ActionResultHandler<TResult, TError> {

    @Override
    public void onSuccess(TResult result) {
        Log.d("default handler onSuccess", result.toString());
    }

    @Override
    public void onError(TError error) {
        Log.d("default handler onError", error.toString());
    }
}
