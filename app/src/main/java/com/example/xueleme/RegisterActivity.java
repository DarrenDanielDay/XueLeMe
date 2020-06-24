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
import com.example.xueleme.models.forms.account.RegisterForm;

import FunctionPackge.Users;
import interface_packge.RegisterHandler;

public class RegisterActivity extends AppCompatActivity {

    private IAccountController accountController = new AccountController(this);
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

                RegisterForm registerForm = new RegisterForm();
                registerForm.mailAddress = username;
                registerForm.password = password;

                accountController.register(new UserAction<>(registerForm, new ActionResultHandler<String, String>() {
                    @Override
                    public void onSuccess(String s) {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                    @Override
                    public void onError(String s) {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }));
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