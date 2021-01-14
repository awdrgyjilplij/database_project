package com.example.databaseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EmployeeShowActivity extends BaseActivity
        implements View.OnClickListener {

    String level = "E";
    Integer employeeID;

    TextView nameTextView;
    TextView employeebranchTextView;
    TextView telephoneTextView;
    TextView emailTextView;

    Button changeinfoButton;
    Button checkinButton;
    Button leaveButton;
    Button returnButton;

    ListView salaryListView;
    ListView checkListView;
    ListView leaveListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_show);

        Intent intent = getIntent();
        String extra_data = intent.getStringExtra("extra_data");
        int extra_id = intent.getIntExtra("extra_id", -1);
        if (extra_id == -1) {
            employeeID = myGlobalData.getEmployeeId();
            level = myGlobalData.getLevel();
        } else {
            employeeID = extra_id;
            level = "M";
        }
        if (extra_data != null) {
            Toast toast = Toast.makeText(EmployeeShowActivity.this,
                    extra_data, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        nameTextView = findViewById(R.id.employee_show_name);
        employeebranchTextView = findViewById(R.id.employee_show_employeebranch);
        telephoneTextView = findViewById(R.id.employee_show_telephone);
        emailTextView = findViewById(R.id.employee_show_email);

        changeinfoButton = findViewById(R.id.employee_show_btn_changeinfo);
        checkinButton = findViewById(R.id.employee_show_btn_checkin);
        leaveButton = findViewById(R.id.employee_show_btn_leave);
        returnButton = findViewById(R.id.employee_show_btn_return);
        changeinfoButton.setOnClickListener(this);
        checkinButton.setOnClickListener(this);
        leaveButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        if (level.equals("M")||level.equals("A"))
            employeebranchTextView.setOnClickListener(this);
        if (myGlobalData.getEmployeeId() != employeeID) {
            if (!myGlobalData.getLevel().equals("A")) {
                changeinfoButton.setVisibility(View.GONE);
                checkinButton.setVisibility(View.GONE);
                leaveButton.setVisibility(View.GONE);
            }
            returnButton.setVisibility(View.GONE);
        }

        salaryListView = findViewById(R.id.employee_show_salary);
        checkListView = findViewById(R.id.employee_show_check);
        leaveListView = findViewById(R.id.employee_show_leave);

        JSONObject postData = new JSONObject();
        postData.put("employeeID", employeeID);
        post("/employeeById", postData.toJSONString(), employeeInfoRecall);
        post("/search/payroll", postData.toJSONString(), employeeSalaryRecall);
        post("/search/attendance", postData.toJSONString(), employeeCheckRecall);
        post("/search/leave", postData.toJSONString(), employeeLeaveRecall);

    }

    private final RecallFunction setBranchRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String employeebranchname = jsonParser.getString("deptName");
        runOnUiThread(() -> {
            employeebranchTextView.setText("部门:" + employeebranchname);
        });
    };

    private final RecallFunction employeeInfoRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String name = jsonParser.getString("name");
        String telephone = jsonParser.getString("telephone");
        String email = jsonParser.getString("email");
        Integer employeebranchId = jsonParser.getInteger("deptID");
        runOnUiThread(() -> {
            nameTextView.setText("姓名:" + name);
            telephoneTextView.setText("电话:" + telephone);
            emailTextView.setText("邮箱:" + email);
        });

        JSONObject postData = new JSONObject();
        postData.put("deptID", employeebranchId);
        post("/search/dept", postData.toJSONString(), setBranchRecall);
    };

    private final RecallFunction employeeSalaryRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        List<String> salary = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = obj.getString("workTime") + ": " + ("" + obj.getDouble("basicSalary")) + "-" +
                    ("" + obj.getDouble("deduction")) + "=" + ("" + obj.getDouble("realSalary")) + "元";
            salary.add(item);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> salaryAdapter = new ArrayAdapter<>(EmployeeShowActivity.this,
                    android.R.layout.simple_list_item_1, salary.toArray(new String[0]));
            salaryListView.setAdapter(salaryAdapter);
        });
    };

    private final RecallFunction employeeCheckRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        List<String> check = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = obj.getString("datetime");
            item = item.substring(0, 10) + " " + item.substring(11, 19);
            if (obj.getInteger("isLate") == 1)
                item += " 迟到";
            else
                item += " 准时";
            check.add(item);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> checkAdapter = new ArrayAdapter<>(EmployeeShowActivity.this,
                    android.R.layout.simple_list_item_1, check.toArray(new String[0]));
            checkListView.setAdapter(checkAdapter);
        });
    };

    private final RecallFunction employeeLeaveRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        List<String> leave = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = obj.getString("leaveBegin") + "-" + obj.getString("leaveEnd") + ": " + obj.getString("leaveReason");
            leave.add(item);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> leaveAdapter = new ArrayAdapter<>(EmployeeShowActivity.this,
                    android.R.layout.simple_list_item_1, leave.toArray(new String[0]));
            leaveListView.setAdapter(leaveAdapter);
        });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.employee_show_btn_changeinfo) {
            View view = View.inflate(EmployeeShowActivity.this, R.layout.employee_changeinfo_view, null);

            final EditText passwordText, telephoneText, emailText;
            passwordText = view.findViewById(R.id.employee_changeinfo_password);
            telephoneText = view.findViewById(R.id.employee_changeinfo_telephone);
            emailText = view.findViewById(R.id.employee_changeinfo_email);
            telephoneText.setText(telephoneTextView.getText().toString().substring(4));
            emailText.setText(emailTextView.getText().toString().substring(4));

            AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeShowActivity.this);
            builder.setTitle("修改信息");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    JSONObject postData = new JSONObject();
                    postData.put("employeeID", employeeID);
                    postData.put("sessionKey", myGlobalData.getSessionKey());
                    postData.put("telephone", telephoneText.getText().toString());
                    postData.put("email", emailText.getText().toString());
                    postData.put("password", passwordText.getText().toString());
                    post("/modifyPersonal", postData.toJSONString(), employeeInfoRecall);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }

        if (v.getId() == R.id.employee_show_btn_checkin) {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date(System.currentTimeMillis()));
            String checkTime="09:00:00";
            if(currentTime.substring(11).compareTo(checkTime)>0){
                Toast toast = Toast.makeText(EmployeeShowActivity.this,
                        "打卡成功", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                JSONObject postData = new JSONObject();
                postData.put("employeeID", employeeID);
                postData.put("isLate", 0);
                post("/attend", postData.toJSONString());
            }
            else{
                Toast toast = Toast.makeText(EmployeeShowActivity.this,
                        String.format("%s 您已迟到", currentTime), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                JSONObject postData = new JSONObject();
                postData.put("employeeID", employeeID);
                postData.put("isLate", 1);
                post("/attend", postData.toJSONString());
            }
        }

        if (v.getId() == R.id.employee_show_btn_leave) {

            View view = View.inflate(EmployeeShowActivity.this, R.layout.employee_leave_view, null);

            final EditText reasonText, startText, endText;
            reasonText = view.findViewById(R.id.employee_leave_reason);
            startText = view.findViewById(R.id.employee_leave_startDate);
            endText = view.findViewById(R.id.employee_leave_endDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeShowActivity.this);
            builder.setTitle("请假");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    JSONObject postData = new JSONObject();
                    postData.put("employeeID", employeeID);
                    postData.put("leaveReason", reasonText.getText().toString());
                    postData.put("applyDay", new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())));
                    postData.put("leaveBegin", startText.getText().toString());
                    postData.put("leaveEnd", endText.getText().toString());
                    post("/leave", postData.toJSONString());

                    Toast toast = Toast.makeText(EmployeeShowActivity.this,
                            "请假成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }

        if (v.getId() == R.id.employee_show_btn_return) {
            Intent intent = new Intent(EmployeeShowActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.employee_show_employeebranch) {
            Intent intent = new Intent(EmployeeShowActivity.this, BranchShowActivity.class);
            startActivity(intent);
        }
    }
}
