package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_account, register_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btn_register = findViewById(R.id.user_register_registerBtn);
        register_account = findViewById(R.id.user_register_account);
        register_password = findViewById(R.id.user_register_password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = register_account.getText().toString();
                String password = register_password.getText().toString();
                Users users = new Users(username, password);
//                try {
//                    String msg = users.Register();
//                    if (msg.equals("注册成功")) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
//                    }
//                    else System.out.println(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

    }
}