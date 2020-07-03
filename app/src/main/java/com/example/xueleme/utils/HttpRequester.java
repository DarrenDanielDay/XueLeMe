package com.example.xueleme.utils;

import android.content.Context;
import android.util.Log;

import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.JSONParser;
import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.responses.BinaryFile;
import com.example.xueleme.models.responses.ServiceResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequester {
    /*
    // developing host
    public static final String HOST = "http://192.168.124.7";
    public static final String PORT = ":5000";
    //*/

    //*
    // production host
    public static final String HOST = "http://129.204.245.98";
    public static final String PORT = ":80";
    //*/

    public static final String BASE_PATH = "/";
    public static String url(String path) {
        return HOST + PORT + BASE_PATH + path;
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
    public final MediaType fileMediaType = MediaType.parse("multipart/form-data");
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

    public void delete(String path, String body, ActionResultHandler<ServiceResult<Object>, String> handler) {
        String fullUrl = url(path);
        Request request = null;
        if (body != null) {
            request = new Request.Builder().url(fullUrl).delete(RequestBody.create(body, mediaType)).build();
        } else {
            request = new Request.Builder().url(fullUrl).delete().build();
        }
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleError(fullUrl, e, handler);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handleResponse(fullUrl, response, ServiceResult.noExtra(), handler);
            }
        });
    }

    public void postFile(File file, ActionResultHandler<BinaryFile, String> handler) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        RequestBody.create(fileMediaType, file)
                )
                .build();
        Request request = new Request.Builder().url(url("api/File/PostFile")).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.onError("上传文件" + file.getName() + "失败：" + FormatHelper.exceptionFormat(e));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.d("postFile", body);
                ServiceResult<BinaryFile> result = ServiceResult.ofGeneric(BinaryFile.class).parse(new Gson().fromJson(body, Map.class));
                handler.onSuccess(result.extraData);
            }
        });
    }

    public void getFile(String md5, File saveDirectory, ActionResultHandler<File, String> handler) {
        File target = new File(saveDirectory.getAbsolutePath() + File.pathSeparator + md5);
        if (target.exists()) {
            handler.onSuccess(target);
            return;
        }
        Request request = new Request.Builder().url(url("api/File/Files/" + md5)).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.onError("下载文件" + md5 + "失败:" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 404) {
                    handler.onError("服务器上找不到指定的文件。");
                }
                InputStream inputStream = response.body().byteStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(target));
                byte[] buffer = new byte[0x400];
                int length = -1;
                while((length = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, length);
                }
                bufferedInputStream.close();
                bufferedOutputStream.close();
                handler.onSuccess(target);
            }
        });
    }
}
