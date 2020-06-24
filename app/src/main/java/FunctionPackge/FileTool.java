package FunctionPackge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import interface_packge.FileHandler;
import interface_packge.ImagePost;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileTool {
    private  String Host="http://darrendanielday.club/";

    public void postImage(File file, ImagePost imagePost)//上传图片
    {   //1.判断是否为图像
        //2.发送文件
        String path=file.getPath();
        int dot = path.lastIndexOf('.');
        String g="";
        if ((dot >-1) && (dot < (path.length() - 1))) {
            g=path.substring(dot + 1);
        }
        //g为文件的扩展名
        int tag=0;
        if(g.equals("bmp") || g.equals("jpg") || g.equals("jpeg")||g.equals("png"))
            tag=1;
        if(tag==0)
        {
            imagePost.IsNotImage();
        }
        String URL="api/File/PostFile";
        MediaType mediaType=MediaType.parse("multipart/form-data");
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody fileBody=RequestBody.create(file,mediaType);
        RequestBody requestBody=new MultipartBody
                .Builder().addFormDataPart("file",file.getName(),fileBody)
                .setType(MediaType.parse("multipart/form-data"))
                .build();
        Request request=new Request.Builder()
                .url(Host+URL)
                .post(requestBody)
                .build();
        final Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                imagePost.postFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String s=response.body().string();
                Gson gson= new Gson();
                Map map=new HashMap<String, Object>();
                map=gson.fromJson(s,map.getClass());
                Double state=(Double)map.get("state");
                if(response.code()!=200||(state!=0&&state!=2))
                {
                    imagePost.postFailed();
                    return;
                }
                else
                {
                    Map<String,Object> mapper= (Map<String, Object>) map.get("extraData");
                    String MD5= (String) mapper.get("mD5");
                    imagePost.postSuccess(MD5);
                }
            }
        });
    }
    private static String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//            cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
//            cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        }
        return cachePath;
    }

    public void downloadImage(String MD5, Context context, FileHandler fileHandler)
    {   String dir=getSystemFilePath(context);
        String URL="api/File/Files";
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        Request request=new Request.Builder()
                .get()
                .url(Host+URL+"/"+MD5)
                .build();
        Call task=okHttpClient.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                fileHandler.DownloadFailed();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code()!=200)
                {
                    fileHandler.DownloadFailed();
                    return;
                }
                InputStream inputStream=response.body().byteStream();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                //Bitmap存到手机里
                File file=new File(dir +"/"+MD5+".png");
                if(file.exists())
                {
                    fileHandler.DownloadSuccess(file);
                    return;
                }
                file.createNewFile();
                FileOutputStream outputStream=new FileOutputStream(file);
                boolean x=bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                if(x)
                {
                    outputStream.flush();
                    outputStream.close();
                    fileHandler.DownloadSuccess(file);
                }
                else
                { fileHandler.DownloadFailed();

                }
            }
        });
    }

}

