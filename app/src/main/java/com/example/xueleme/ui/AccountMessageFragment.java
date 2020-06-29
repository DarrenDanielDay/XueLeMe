package com.example.xueleme.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.xueleme.LoginActivity;
import com.example.xueleme.MainActivity;
import com.example.xueleme.NotificationActivity;
import com.example.xueleme.R;
import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.DefaultHandler;
import com.example.xueleme.business.FileController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.IFileController;
import com.example.xueleme.business.INotificationController;
import com.example.xueleme.business.NotificationController;
import com.example.xueleme.business.Subscriber;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.account.ChangeAvatarForm;
import com.example.xueleme.models.forms.account.ChangeNicknameForm;
import com.example.xueleme.models.locals.Notification;
import com.example.xueleme.models.locals.NotificationTypeEnum;
import com.example.xueleme.models.locals.User;
import com.example.xueleme.models.responses.UserDetail;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import FunctionPackge.FileTool;
import interface_packge.ImagePost;

public class AccountMessageFragment extends Fragment {
    ListView listView;
    ImageView imageView;
    IAccountController iAccountController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        listView = root.findViewById(R.id.line1);
        final int[] heads = new int[]{R.drawable.ic_baseline_person_pin_24, R.drawable.ic_baseline_create_24, R.drawable.ic_baseline_comment_24, R.drawable.ic_baseline_cancel_24};
        final String[] strings = {"头像", "修改昵称", "通知", "退出登录"};
        iAccountController = new AccountController(getActivity());
        //iNotificationController=new NotificationController();
        List list = new ArrayList();
        imageView = root.findViewById(R.id.imageView);
        disaplayFile(imageView);
        for (int i = 0; i < strings.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", heads[i]);
            map.put("content", strings[i]);
            list.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.item_account, new String[]{"icon", "content"}, new int[]{R.id.imageView, R.id.textView5});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    iAccountController.logout(new UserAction<>(null, new DefaultHandler<>()));
                    getActivity().finish();
                } else if (position == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("输入新昵称：");
                    builder.setIcon(R.drawable.ic_baseline_create_24);
                    final EditText editText = new EditText(getActivity());
                    builder.setView(editText);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nickname = editText.getText().toString();
                            IAccountController iAccountController = new AccountController(getActivity());
                            User user = iAccountController.getCurrentUser();
                            ChangeNicknameForm changeNicknameForm = new ChangeNicknameForm();
                            changeNicknameForm.nickname = nickname;
                            changeNicknameForm.userId = user.id;
                            iAccountController.changeNickname(new UserAction<>(changeNicknameForm, new ActionResultHandler<String, String>() {
                                @Override
                                public void onSuccess(String s) {
                                    Looper.prepare();
                                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                                @Override
                                public void onError(String s) {
                                    Looper.prepare();
                                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }));
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.create().show();
                } else if (position == 0) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    }
                }
                else if(position==2)
                {
                    Intent intent=new Intent(getContext(),NotificationActivity.class);
                    startActivity(intent);
                }
            }

        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("re", String.valueOf(requestCode));
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(getActivity(), "无权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String path = new String();
        switch (requestCode) {
            case 2:
                if (Build.VERSION.SDK_INT >= 19) {
                    path = handleImageOnKitKat(data);
                } else {
                    path = handleImageBeforeKitKat(data);
                }
                File file = new File(path);
                Log.d("dir",file.getAbsolutePath());
                FileTool fileTool = new FileTool();
                fileTool.postImage(file, new ImagePost() {
                    @Override
                    public void postFailed() {
                        Looper.prepare();
                        Toast.makeText(getContext(), "上传图片失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void IsNotImage() {
                        Toast.makeText(getContext(), "文件格式错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void postSuccess(String Md5) {
                        ChangeAvatarForm changeAvatarForm = new ChangeAvatarForm();
                        IAccountController iAccountController = new AccountController(getActivity());
                        User user = iAccountController.getCurrentUser();
                        changeAvatarForm.avatar = Md5;
                        changeAvatarForm.userId = user.id;
                        iAccountController.changeAvatar(new UserAction<>(changeAvatarForm, new ActionResultHandler<String, String>() {
                            @Override
                            public void onSuccess(String s) {
                                Looper.prepare();
                                Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                disaplayFile(imageView);
                            }

                            @Override
                            public void onError(String s) {
                                Looper.prepare();
                                Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }));
                    }
                });
                break;
            default:
                break;
        }
    }

    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
        }
        return imagePath;
    }

    private String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void disaplayFile(ImageView imageView) {

        User users = iAccountController.getCurrentUser();
        Integer userId=users.id;
        iAccountController.queryUserDetailFromId(new UserAction<>(userId, new ActionResultHandler<UserDetail, String>() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                String avatar=userDetail.avatar;
                IFileController iFileController = new FileController(getContext());
                iFileController.getFile(new UserAction<>(avatar, new ActionResultHandler<File, String>() {
                    @Override
                    public void onSuccess(File file) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                        getActivity().runOnUiThread(()->imageView.setImageBitmap(bitmap));
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(getContext(), "图片显示失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }));
            }

            @Override
            public void onError(String s) {
                Looper.prepare();
                Toast.makeText(getContext(), "图片显示失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }));

    }
}
