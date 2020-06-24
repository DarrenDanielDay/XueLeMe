package com.example.xueleme.utils;

import android.util.Log;

import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.models.JSONParser;
import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.responses.ServiceResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequester {
    public static final String HOST = "http://129.204.245.98";
    public static final String BASE_PATH = "/";
    public static String url(String path) {
        return HOST + BASE_PATH + path;
    }
    private HttpRequester() {}
    private static HttpRequester httpRequester = null;
    public static HttpRequester getInstance() {
        if (httpRequester == null) {
            httpRequester = new HttpRequester();
        }
        return httpRequester;
    }

    public final MediaType mediaType = MediaType.parse("application/json");
    public final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS).build();
    public  <TResult extends ReflectiveJSONModel<TResult>>
    void handleResponse(
            String fullUrl,
            Response response,
            JSONParser<TResult> parser,
            ActionResultHandler<TResult, String> handler) {
        try {
            String bodyString = response.body().string();
            Log.d("Request result", bodyString);
            Gson gson = new Gson();
            Object json = gson.fromJson(bodyString, Map.class);
            TResult result = parser.parse(json);
            handler.onSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            handler.onError("处理JSON出错:" + e.getMessage());
        }
    }

    public  <TResult extends ReflectiveJSONModel<TResult>>
    void handleError(String fullUrl, Exception e, ActionResultHandler<TResult, String> handler) {
        e.printStackTrace();
        Log.e("Request failed", fullUrl + " " + e.getMessage());
        handler.onError("请求失败");
    }

    public  <TResult extends ReflectiveJSONModel<TResult>>

    void get(String path, JSONParser<TResult> parser, ActionResultHandler<TResult, String> handler) {
        String fullUrl = url(path);
        Log.d("HttpRequester", "get " + fullUrl);
        Request request = new Request.Builder().url(fullUrl).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleError(fullUrl, e, handler);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handleResponse(fullUrl, response, parser, handler);
            }
        });
    }

    public  <TData extends ReflectiveJSONModel<TData>, TResult extends ReflectiveJSONModel<TResult>>
    void post(String path, TData data, JSONParser<TResult> parser, ActionResultHandler<TResult, String> handler) {
        String bodyString = String.valueOf(data.serialize());
        RequestBody body = RequestBody.create(bodyString, mediaType);
        String fullUrl = url(path);
        Log.d("HttpRequester", "post " + fullUrl + " form = " + bodyString);
        Request request = new Request.Builder().url(fullUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleError(fullUrl, e, handler);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handleResponse(fullUrl, response, parser, handler);
            }
        });
    }
}
