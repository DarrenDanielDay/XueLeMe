package com.example.xueleme;

import android.util.Log;

import com.example.xueleme.models.responses.BriefGroup;
import com.example.xueleme.models.responses.ServiceResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONParserTest {
    @Test
    public void testRecursiveParse() {
        Request request = new Request.Builder().url("http://darrendanielday.club/api/ChatGroup/MyJoinedGroup/2").get().build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("test failed", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {

                String string = response.body().string();
                Gson gson = new Gson();
                Map<String, Object> json = gson.fromJson(string, Map.class);
                List<BriefGroup> list = new ArrayList<BriefGroup>();
                ServiceResult<List> userDetail = new ServiceResult<List>(List.class, BriefGroup.class).parse(json);
                Log.d("test success", userDetail.toString());
                throw new IOException("");
                }catch (Exception e) {
                    Assert.fail();
                }
            }
        });
    }
}
