package com.example.xueleme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.xueleme.AskImageFileActivity;
import com.example.xueleme.BaseActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.FileController;
import com.example.xueleme.business.IFileController;
import com.example.xueleme.business.UserAction;

import java.io.File;
import java.util.function.Consumer;

public class ImageHelper {

    public synchronized static void setImageView(Activity activity, ImageView imageView, String imageMD5) {
        IFileController fileController = new FileController(activity);
        fileController.getFile(new UserAction<>(imageMD5, new ActionResultHandler<File, String>() {
            @Override
            public void onSuccess(File file) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                activity.runOnUiThread(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            }

            @Override
            public void onError(String s) {
                activity.runOnUiThread(() -> {
                    imageView.setImageResource(R.drawable.ic_baseline_cancel_24);
                });
            }
        }));
    }

    public static void askImageFile(Context context, Consumer<File> onSuccess, Consumer<Throwable> onError) {
        AskImageFileActivity.setOnSuccess(onSuccess);
        AskImageFileActivity.setOnError(onError);
        context.startActivity(new Intent(context, AskImageFileActivity.class));
    }
}
