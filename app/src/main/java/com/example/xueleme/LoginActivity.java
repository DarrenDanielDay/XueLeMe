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
import com.example.xueleme.models.forms.account.LoginForm;
import com.example.xueleme.models.locals.User;

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

                LoginForm loginForm = new LoginForm();
                loginForm.mailAddress = username;
                loginForm.password = password;

                IAccountController accountController = new AccountController();
                accountController.login(new UserAction<>(loginForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String s) {
                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                }));
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