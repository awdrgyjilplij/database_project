package com.example.databaseproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.btn_login);
        Button signupButton = findViewById(R.id.btn_signup);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        ProgressBar loadingProgressBar = findViewById(R.id.loading);
    }

    String username, password;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_login) {

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (username.equals("") || password.equals("")) {
                //TODO:error please input
                return;
            }

            // send sign up request
            JSONObject postData = new JSONObject();
            postData.put("username", username);
            postData.put("password", password);
            Logger.d("LOGIN_DEBUG", postData.toJSONString());
            post("signUp", postData.toJSONString(), logInRecall);
        }
        else{
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }


    private RecallFunction logInRecall = (s)->{
        Logger.d("LOGIN_DEBUG", s);

        JSONObject jsonParser = JSONObject.parseObject(s);
        Integer id = jsonParser.getInteger("employeeid");

        // TODO:check

        // TODO:E,M,A
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    };
}