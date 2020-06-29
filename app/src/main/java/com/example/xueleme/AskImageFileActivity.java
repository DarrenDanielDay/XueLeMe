package com.example.xueleme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.util.function.Consumer;

public class AskImageFileActivity extends AppCompatActivity {
    public static Consumer<File> onSuccess;
    public static Consumer<Throwable> onError;
    public static synchronized void setOnSuccess(Consumer<File> onSuccess) {
        AskImageFileActivity.onSuccess = onSuccess;
    }

    public static synchronized void setOnError(Consumer<Throwable> onError) {
        AskImageFileActivity.onError = onError;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 0x110);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0x110 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                File file = new File(picturePath);
                onSuccess.accept(file);
            } else {
                throw new Exception("用户取消了选择");
            }
        } catch (Throwable throwable) {
            onError.accept(throwable);
        }
        this.finish();
    }

}