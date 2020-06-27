package com.example.xueleme.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.widget.ImageView;

import com.example.xueleme.R;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.FileController;
import com.example.xueleme.business.IFileController;
import com.example.xueleme.business.UserAction;

import java.io.File;

public class ImageHelper {

    public synchronized static void setImageView(Activity context, ImageView imageView, String imageMD5) {
        IFileController fileController = new FileController(context);
        fileController.getFile(new UserAction<>(imageMD5, new ActionResultHandler<File, String>() {
            @Override
            public void onSuccess(File file) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                context.runOnUiThread(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            }

            @Override
            public void onError(String s) {
                context.runOnUiThread(() -> {
                    imageView.setImageResource(R.drawable.ic_baseline_cancel_24);
                });
            }
        }));
    }
}
