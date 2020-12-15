package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.preference.PreferenceFragmentCompat;

import com.alibaba.fastjson.JSONObject;

public class SignupActivity extends BaseActivity
        implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText birthdayEditText;
    private EditText socialIDEditText;
    private EditText hiredateEditText;
    private EditText genderEditText;
    private EditText deptIDEditText;
    private EditText telephoneEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button sureButton=findViewById(R.id.reg_btn_sure);
        Button returnButton=findViewById(R.id.reg_btn_ret);
        sureButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        usernameEditText = findViewById(R.id.reg_username);
        passwordEditText = findViewById(R.id.reg_password);
        nameEditText = findViewById(R.id.reg_name);
        birthdayEditText = findViewById(R.id.reg_birthday);
        socialIDEditText = findViewById(R.id.reg_socialID);
        hiredateEditText = findViewById(R.id.reg_hiredate);
        genderEditText = findViewById(R.id.reg_gender);
        deptIDEditText = findViewById(R.id.reg_deptID);
        telephoneEditText = findViewById(R.id.reg_telephone);
        emailEditText = findViewById(R.id.reg_email);
    }

    String name,birthday,socialID,hiredate,username,password,gender,deptID,telephone,email;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_sure) {

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            name = nameEditText.getText().toString();
            birthday = birthdayEditText.getText().toString();
            socialID = socialIDEditText.getText().toString();
            hiredate = hiredateEditText.getText().toString();
            gender = genderEditText.getText().toString();
            deptID = deptIDEditText.getText().toString();
            telephone = telephoneEditText.getText().toString();
            email = emailEditText.getText().toString();

            //TODO: register


            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}