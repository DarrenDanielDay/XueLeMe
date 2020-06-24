package com.example.xueleme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.IAccountController;

public class StartActivity extends AppCompatActivity {
    private IAccountController accountController = new AccountController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // token验证
        if (accountController.getCurrentUser() == null) {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
