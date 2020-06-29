package com.example.xueleme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.account.ForgetPasswordForm;

public class ForgetPasswordActivity extends BaseActivity {

    private EditText forget_account;
    IAccountController accountController = new AccountController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Button btn_forget = findViewById(R.id.user_forget_forgetBtn);
        Button btn_goto_login = findViewById(R.id.user_forget_loginBtn);
        forget_account = findViewById(R.id.user_forget_account);
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = forget_account.getText().toString();
                ForgetPasswordForm forgetPasswordForm = new ForgetPasswordForm();
                forgetPasswordForm.mailAddress = account;
                accountController.forgetPassword(new UserAction<>(forgetPasswordForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Looper.prepare();
                        Toast.makeText(ForgetPasswordActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(ForgetPasswordActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
            }
        });
        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}