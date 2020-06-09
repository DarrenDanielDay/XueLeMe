package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText login_account, login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_login= findViewById(R.id.user_login_loginBtn);
        login_account = findViewById(R.id.user_login_account);
        login_password = findViewById(R.id.user_login_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = login_account.getText().toString();
                String password = login_password.getText().toString();
                Users users = new Users(username, password);
                try {
                    String msg = users.Login();
                    if (msg.equals("密码正确")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else System.out.println(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}