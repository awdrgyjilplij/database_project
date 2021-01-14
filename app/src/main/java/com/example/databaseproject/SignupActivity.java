package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONObject;

public class SignupActivity extends BaseActivity
        implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText birthdayEditText;
    private EditText socialIDEditText;
    private EditText hiredateEditText;
    private RadioButton genderButton_M;
    private EditText deptIDEditText;
    private EditText telephoneEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button sureButton = findViewById(R.id.reg_btn_sure);
        Button returnButton = findViewById(R.id.reg_btn_ret);
        sureButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        usernameEditText = findViewById(R.id.reg_username);
        passwordEditText = findViewById(R.id.reg_password);
        nameEditText = findViewById(R.id.reg_name);
        birthdayEditText = findViewById(R.id.reg_birthday);
        socialIDEditText = findViewById(R.id.reg_socialID);
        hiredateEditText = findViewById(R.id.reg_hiredate);
        genderButton_M = findViewById(R.id.reg_gender_M);
        deptIDEditText = findViewById(R.id.reg_deptID);
        telephoneEditText = findViewById(R.id.reg_telephone);
        emailEditText = findViewById(R.id.reg_email);
    }

    String name, birthday, socialID, hiredate, username, password, deptID, telephone, email;
    Character gender;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_sure) {

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            name = nameEditText.getText().toString();
            birthday = birthdayEditText.getText().toString();
            socialID = socialIDEditText.getText().toString();
            hiredate = hiredateEditText.getText().toString();
            if (genderButton_M.isChecked())
                gender = 'M';
            else
                gender = 'F';
            deptID = deptIDEditText.getText().toString();
            telephone = telephoneEditText.getText().toString();
            email = emailEditText.getText().toString();

            JSONObject postData = new JSONObject();
            postData.put("username", username);
            postData.put("password", password);
            postData.put("name", name);
            postData.put("birthday", birthday);
            postData.put("socialID", socialID);
            postData.put("hiredate", hiredate);
            postData.put("gender", gender);
            postData.put("deptID", deptID);
            postData.put("telephone", telephone);
            postData.put("email", email);
            post("/signup", postData.toJSONString(), signupRecall);

        } else {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private final RecallFunction signupRecall = (s) -> {

        JSONObject jsonParser = JSONObject.parseObject(s);

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    };
}