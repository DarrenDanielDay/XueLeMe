package com.example.xueleme.utils;

import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.models.ReflectiveJSONModel;

import okhttp3.Request;

public class HttpRequester<TData, TResult extends ReflectiveJSONModel> {
    void post(TData data, Class<TResult> resultClass, ActionResultHandler<TResult, String> handler) {
        Request request = null;
    }
}
