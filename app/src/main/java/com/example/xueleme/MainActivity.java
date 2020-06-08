package com.example.xueleme;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xueleme.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1=findViewById(R.id.id1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Users users=new Users("123@qq.com","545606");
           Task task =new Task(MainActivity.this,users);
           Log.d("Maincaitivity",task.create(2021,6,5,10,20,19,30,"高等数学"));
                Log.d("Maincaitivity",task.create(2021,6,5,10,20,19,30,"DIP"));
                Log.d("Maincaitivity",task.create(2021,6,5,10,20,19,30,"计算机视觉"));
                Log.d("Maincaitivity",task.create(2021,6,5,10,20,19,30,"算法导论"));
                List<String> task_message=task.query(2021,6,5);
                Log.d("Maincaitivity","-------------------------------------------------------------");
                for(int i=0;i<=task_message.size()-1;i++)
                {
                    Log.d("Maincaitivity",task_message.get(i));
                }
                task.updata(20,13,23,16,"QER");
                 task_message=task.query(2021,6,5);
                Log.d("Maincaitivity","-------------------------------------------------------------");
                for(int i=0;i<=task_message.size()-1;i++)
                {
                    Log.d("Maincaitivity",task_message.get(i));
                }
                task.delete(2021,6,5,"DIP");
                task_message=task.query(2021,6,5);
                Log.d("Maincaitivity","-------------------------------------------------------------");
                for(int i=0;i<=task_message.size()-1;i++)
                {
                    Log.d("Maincaitivity",task_message.get(i));
                }

            }
        });


    }
}
