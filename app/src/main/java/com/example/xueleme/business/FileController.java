package com.example.xueleme.business;

import android.content.Context;

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
