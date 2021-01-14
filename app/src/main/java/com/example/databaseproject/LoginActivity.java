package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;

    String username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.btn_login);
        Button signupButton = findViewById(R.id.btn_signup);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        usernameEditText.setText(username);
        passwordEditText.setText(password);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_login) {

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (username.equals("") || password.equals("")) {
                Toast toast = Toast.makeText(LoginActivity.this, "please input username ans password", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            JSONObject postData = new JSONObject();
            postData.put("username", username);
            postData.put("password", password);
            Logger.d("username", username);
            Logger.d("password", password);
            post("/login", postData.toJSONString(), logInRecall);

        } else {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    private final RecallFunction setManagerRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String managername = jsonParser.getString("name");
        myGlobalData.setBranchManagerName(managername);
    };

    private final RecallFunction setBranchRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String branchname = jsonParser.getString("deptName");
        Integer managerId = jsonParser.getInteger("managerID");
        myGlobalData.setBranchName(branchname);

        JSONObject postData = new JSONObject();
        postData.put("employeeID", managerId);
        post("/employeeById", postData.toJSONString(), setManagerRecall);
    };

    private final RecallFunction logInRecall = (s) -> {

        JSONObject jsonParser = JSONObject.parseObject(s);
        Integer id = jsonParser.getInteger("employeeID");
        String level = jsonParser.getString("level");
        String name = jsonParser.getString("name");
        Integer branchid = jsonParser.getInteger("deptID");
        Integer sessionkey = jsonParser.getInteger("sessionKey");

        if ((id + "").equals("null")) {
            Looper.prepare();
            Toast toast = Toast.makeText(LoginActivity.this, "wrong username or password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Looper.loop();
            return;
        }

        myGlobalData.setEmployeeId(id);
        myGlobalData.setEmployeeName(name);
        myGlobalData.setBranchId(branchid);
        myGlobalData.setSessionKey(sessionkey);
        myGlobalData.setLevel(level);

        JSONObject postData = new JSONObject();
        postData.put("deptID", branchid);
        post("/search/dept", postData.toJSONString(), setBranchRecall);

        if (level.equals("E")) {
            Intent intent = new Intent(LoginActivity.this, EmployeeShowActivity.class);
            intent.putExtra("extra_data", "welcome employee");
            startActivity(intent);
        }

        if (level.equals("M")) {
            Intent intent = new Intent(LoginActivity.this, BranchShowActivity.class);
            intent.putExtra("extra_data", "welcome manager");
            startActivity(intent);
        }

        if (level.equals("A")) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            intent.putExtra("extra_data", "welcome administrator");
            startActivity(intent);
        }
    };
}