package com.example.xueleme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueleme.business.AccountController;
import com.example.xueleme.business.ActionResultHandler;
import com.example.xueleme.business.IAccountController;
import com.example.xueleme.business.UserAction;
import com.example.xueleme.models.forms.account.LoginForm;


public class LoginActivity extends BaseActivity {

    private EditText login_account, login_password;

    private IAccountController accountController = new AccountController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_login = findViewById(R.id.user_login_loginBtn);
        Button btn_goto_register = findViewById(R.id.user_login_register);
        Button btn_goto_forget = findViewById(R.id.user_login_find_password);
        login_account = findViewById(R.id.user_login_account);
        login_password = findViewById(R.id.user_login_password);

        btn_login.setOnClickListener(v -> {

            final String username = login_account.getText().toString();
            final String password = login_password.getText().toString();

            LoginForm loginForm = new LoginForm();
            loginForm.mailAddress = username;
            loginForm.password = password;

            accountController.login(new UserAction<>(loginForm, new ActionResultHandler<String, String>() {
                @Override
                public void onSuccess(String s) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String s) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }));
        });
        btn_goto_register.setOnClickListener(V -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        btn_goto_forget.setOnClickListener(V -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }
}