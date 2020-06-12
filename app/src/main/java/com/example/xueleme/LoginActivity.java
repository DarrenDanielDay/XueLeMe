package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import interface_packge.LoginHandler;

public class LoginActivity extends AppCompatActivity {

    private EditText login_account, login_password;

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
                String username = login_account.getText().toString();
                String password = login_password.getText().toString();
                Users users = new Users(username, password);
                users.setLoginHandler(new LoginHandler() {
                    @Override
                    public void password_correct() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void connection_failed() {
                        Toast.makeText(LoginActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void password_wrong() {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();                    }

                    @Override
                    public void account_isnull() {
                        Toast.makeText(LoginActivity.this, "账户为空", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void password_isnull() {
                        Toast.makeText(LoginActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void JSON_error() {
                        Toast.makeText(LoginActivity.this, "JSON错误", Toast.LENGTH_LONG).show();
                    }
                });
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