package com.example.xueleme.business;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xueleme.models.responses.BinaryFile;
import com.example.xueleme.utils.HttpRequester;

import java.io.File;

public class FileController extends RequestController implements IFileController {
    private final Context context;

    public FileController(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void postFile(UserAction<File, String, String> action) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        HttpRequester.getInstance().postFile(action.data, new ActionResultHandler<BinaryFile, String>() {
            @Override
            public void onSuccess(BinaryFile binaryFile) {
                action.resultHandler.onSuccess(binaryFile.mD5);
            }

            @Override
            public void onError(String s) {
                action.resultHandler.onError(s);
            }
        });
    }

    @Override
    public void getFile(UserAction<String, File, String> action) {
        HttpRequester.getInstance().getFile(action.data, context.getCacheDir(), action.resultHandler);
    }
}
