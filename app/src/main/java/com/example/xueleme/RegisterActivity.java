package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import FunctionPackge.Users;
import interface_packge.RegisterHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_account, register_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btn_register = findViewById(R.id.user_register_registerBtn);
        Button btn_goto_login = findViewById(R.id.user_register_loginBtn);
        register_account = findViewById(R.id.user_register_account);
        register_password = findViewById(R.id.user_register_password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = register_account.getText().toString();
                String password = register_password.getText().toString();
                Users users = new Users(username, password);
                users.setRegisterHandler(new RegisterHandler() {
                    @Override
                    public void alreadyRegister() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "已注册", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void successRegister() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void requestFailed() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "请求错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void accountIsnull() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "账户为空", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void passwordIsnull() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void JSONError() {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "JSON错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
                users.Register();
            }
        });
        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}