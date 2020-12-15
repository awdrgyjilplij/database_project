package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmployeeSetActivity extends BaseActivity
        implements View.OnClickListener {

    private EditText passwordEditText;
    private EditText telephoneEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_set);
        Button sureButton=findViewById(R.id.set_btn_sure);
        Button returnButton=findViewById(R.id.set_btn_ret);
        sureButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        passwordEditText = findViewById(R.id.set_password);
        telephoneEditText = findViewById(R.id.set_telephone);
        emailEditText = findViewById(R.id.set_email);
    }

    String password,telephone,email;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_sure) {

            password = passwordEditText.getText().toString();
            telephone = telephoneEditText.getText().toString();
            email = emailEditText.getText().toString();

            //TODO: setting

            Intent intent = new Intent(EmployeeSetActivity.this, EmployeeShowActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(EmployeeSetActivity.this, EmployeeShowActivity.class);
            startActivity(intent);
        }
    }

}