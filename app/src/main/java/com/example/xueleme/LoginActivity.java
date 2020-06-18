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
import interface_packge.LoginHandler;

public class LoginActivity extends AppCompatActivity {

    private EditText login_account, login_password;
    public static Users users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_login= findViewById(R.id.user_login_loginBtn);
        Button btn_goto_register = findViewById(R.id.user_login_register);
        login_account = findViewById(R.id.user_login_account);
        login_password = findViewById(R.id.user_login_password);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String username = login_account.getText().toString();
                final String password = login_password.getText().toString();
                users = new Users(username, password);

                users.setLoginHandler(new LoginHandler() {
                    @Override
                    public void password_correct() {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("extra_data", username+" "+password);
                        startActivity(intent);
                    }

                    @Override
                    public void connection_failed() {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void password_wrong() {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void account_isnull() {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "账户为空", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void password_isnull() {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void JSON_error() {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "JSON错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
                users.Login();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
        btn_goto_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}