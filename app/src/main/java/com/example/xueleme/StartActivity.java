package com.example.xueleme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.services.NotificationService;

public class StartActivity extends BaseActivity {
    private IAccountController accountController = new AccountController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startService(new Intent(this, NotificationService.class));
        // token验证
        if (accountController.getCurrentUser() == null) {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
