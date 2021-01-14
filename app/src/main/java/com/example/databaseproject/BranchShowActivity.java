package com.example.databaseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BranchShowActivity extends BaseActivity
        implements View.OnClickListener {

    private Integer branchID;
    private Integer branchManagerID;
    private Integer employeeID;
    private String level;

    List<String> employee = new ArrayList<>();
    List<Integer> employeeId = new ArrayList<>();
    List<String> salary = new ArrayList<>();
    List<Integer> salaryId = new ArrayList<>();
    List<String> leave = new ArrayList<>();
    List<Integer> leaveId = new ArrayList<>();
    List<Integer> abnormal_employeeId = new ArrayList<>();

    TextView branchnameTextView;
    TextView branchIDTextView;
    TextView branchdescriptionTextView;
    TextView branchmanagerTextView;

    Button changeinfoButton;
    Button checksalaryButton;
    Button checkleaveButton;
    Button returnButton;
    Button adminButton;

    ListView employeeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_show);

        Intent intent = getIntent();
        String extra_data = intent.getStringExtra("extra_data");
        int extra_id = intent.getIntExtra("extra_id", -1);

        if (extra_id == -1)
            branchID = myGlobalData.getBranchId();
        else
            branchID = extra_id;

        employeeID = myGlobalData.getEmployeeId();
        level = myGlobalData.getLevel();

        if (extra_data != null) {
            Toast toast = Toast.makeText(BranchShowActivity.this,
                    extra_data, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        changeinfoButton = findViewById(R.id.branch_show_btn_changeinfo);
        checksalaryButton = findViewById(R.id.branch_show_btn_checksalary);
        checkleaveButton = findViewById(R.id.branch_show_btn_checkleave);
        returnButton = findViewById(R.id.branch_show_btn_return);
        adminButton = findViewById(R.id.branch_show_btn_admin);
        changeinfoButton.setOnClickListener(this);
        checksalaryButton.setOnClickListener(this);
        checkleaveButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        adminButton.setOnClickListener(this);
        if (!level.equals("A"))
            adminButton.setVisibility(View.GONE);

        branchnameTextView = findViewById(R.id.branch_show_name);
        branchIDTextView = findViewById(R.id.branch_show_branchID);
        branchdescriptionTextView = findViewById(R.id.branch_show_branchdescription);
        branchmanagerTextView = findViewById(R.id.branch_show_branchmanager);
        branchdescriptionTextView.setOnClickListener(this);
        employeeListView = findViewById(R.id.branch_show_employeeinfo);

        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer employeeid = employeeId.get(position);
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(BranchShowActivity.this);
                alertdialogbuilder.setMessage(employee.get(position));

                alertdialogbuilder.setPositiveButton("查看员工", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BranchShowActivity.this, EmployeeShowActivity.class);
                        intent.putExtra("extra_data", "welcome administrator");
                        intent.putExtra("extra_id", employeeid);
                        startActivity(intent);
                    }
                });

                alertdialogbuilder.setNeutralButton("删除员工", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(BranchShowActivity.this);
                        alertdialogbuilder.setMessage("确认删除");
                        alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                employee.remove(position);
                                employeeId.remove(position);
                                ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(BranchShowActivity.this,
                                        android.R.layout.simple_list_item_1, employee.toArray(new String[0]));
                                employeeListView.setAdapter(branchAdapter);

                                JSONObject postData = new JSONObject();
                                postData.put("employeeID", employeeid);
                                post("/admin/deleteEmployee", postData.toJSONString());
                            }
                        });
                        alertdialogbuilder.setNegativeButton("取消", null);
                        alertdialogbuilder.create().show();
                    }
                });

                alertdialogbuilder.setNegativeButton("取消", null);
                alertdialogbuilder.create().show();
            }
        });

        JSONObject postData = new JSONObject();
        postData.put("deptID", branchID);
        post("/search/dept", postData.toJSONString(), branchInfoRecall);
        post("/employeeByDept", postData.toJSONString(), branchEmployeeRecall);
        post("/manager/payroll", postData.toJSONString(), branchSalaryRecall);
        post("/manager/leave", postData.toJSONString(), branchLeaveRecall);
        post("/manager/warning", postData.toJSONString(), branchAbnormalRecall);
    }

    private final RecallFunction branchAbnormalRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            abnormal_employeeId.add(obj.getInteger("employeeID"));
        }
    };

    private final RecallFunction setManagerRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String managername = jsonParser.getString("name");
        branchmanagerTextView.setText("经理:" + managername);
    };

    private final RecallFunction branchInfoRecall = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        String name = jsonParser.getString("deptName");
        String description = jsonParser.getString("description");
        Integer managerId = jsonParser.getInteger("managerID");
        branchManagerID = managerId;

        runOnUiThread(() -> {
            branchIDTextView.setText("编号:" + branchID.toString());
            branchnameTextView.setText("名称:" + name);
            branchdescriptionTextView.setText("描述:" + description);
        });

        JSONObject postData = new JSONObject();
        postData.put("employeeID", managerId);
        post("/employeeById", postData.toJSONString(), setManagerRecall);
    };

    private final RecallFunction branchEmployeeRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = ("" + obj.getInteger("employeeID")) + " " + obj.getString("name");
            if (obj.getString("gender").equals("M"))
                item += " 男";
            else
                item += "女";
            if (abnormal_employeeId.contains(obj.getInteger("employeeID")))
                item += " 异常";
            employee.add(item);
            employeeId.add(obj.getInteger("employeeID"));
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(BranchShowActivity.this,
                    android.R.layout.simple_list_item_1, employee.toArray(new String[0]));
            employeeListView.setAdapter(employeeAdapter);
        });

    };

    private final RecallFunction branchSalaryRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = ("" + obj.getInteger("employeeID")) + ": " + obj.getString("workTime") + " "
                    + ("" + obj.getDouble("basicSalary")) + "-" + ("" + obj.getDouble("deduction"))
                    + "=" + ("" + obj.getDouble("realSalary"));
            salary.add(item);
            salaryId.add(obj.getInteger("salaryNo"));
        }
    };

    private final RecallFunction branchLeaveRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = ("" + obj.getInteger("employeeID")) + ": " + obj.getString("leaveBegin")
                    + "-" + obj.getString("leaveEnd") + " " + obj.getString("leaveReason");
            leave.add(item);
            leaveId.add(obj.getInteger("leaveNo"));
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.branch_show_btn_changeinfo) {
            View view = View.inflate(BranchShowActivity.this, R.layout.branch_changeinfo_view, null);

            Logger.d("employeeID", employeeID + "");

            EditText nameText, descriptionText, managerIdText;
            nameText = view.findViewById(R.id.branch_changeinfo_name);
            descriptionText = view.findViewById(R.id.branch_changeinfo_description);
            managerIdText = view.findViewById(R.id.branch_changeinfo_managerId);
            nameText.setText(branchnameTextView.getText().toString().substring(3));
            managerIdText.setText(branchManagerID + "");
            if (!level.equals("A")) {
                managerIdText.setKeyListener(null);
                managerIdText.setBackground(null);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(BranchShowActivity.this);
            builder.setTitle("修改信息");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    JSONObject postData = new JSONObject();
                    postData.put("deptID", branchID);
                    postData.put("deptName", nameText.getText().toString());
                    postData.put("description", descriptionText.getText().toString());
                    post("/manager/dept", postData.toJSONString());

                    if (level.equals("A")) {
                        postData = new JSONObject();
                        postData.put("deptID", branchID);
                        postData.put("managerID", managerIdText.getText().toString());
                        post("/admin/setManager", postData.toJSONString());
                    }

                    postData = new JSONObject();
                    postData.put("deptID", branchID);
                    post("/search/dept", postData.toJSONString(), branchInfoRecall);
                }
            });
            builder.create().show();
        }

        if (v.getId() == R.id.branch_show_btn_checksalary) {
            View view = View.inflate(BranchShowActivity.this, R.layout.branch_salary_view, null);

            ListView salaryListView;
            salaryListView = view.findViewById(R.id.branch_show_salary);
            ArrayAdapter<String> salaryAdapter = new ArrayAdapter<String>(BranchShowActivity.this, android.R.layout.simple_list_item_1, salary);
            salaryListView.setAdapter(salaryAdapter);

            salaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int salaryid = salaryId.get(position);
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(BranchShowActivity.this);
                    alertdialogbuilder.setMessage("确认审批");
                    alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            salary.remove(position);
                            salaryId.remove(position);
                            salaryListView.setAdapter(salaryAdapter);

                            JSONObject postData = new JSONObject();
                            postData.put("salaryNo", salaryid);
                            postData.put("verifierID", myGlobalData.getEmployeeId());
                            post("/manager/verify", postData.toJSONString());
                        }
                    });
                    alertdialogbuilder.setNegativeButton("取消", null);
                    alertdialogbuilder.create().show();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(BranchShowActivity.this);
            builder.setTitle("审批工资");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);

            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

        if (v.getId() == R.id.branch_show_btn_checkleave) {
            View view = View.inflate(BranchShowActivity.this, R.layout.branch_leave_view, null);

            ListView leaveListView;
            leaveListView = view.findViewById(R.id.branch_show_leave);
            ArrayAdapter<String> leaveAdapter = new ArrayAdapter<>(BranchShowActivity.this, android.R.layout.simple_list_item_1, leave);
            leaveListView.setAdapter(leaveAdapter);

            leaveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int leaveid = leaveId.get(position);
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(BranchShowActivity.this);
                    alertdialogbuilder.setMessage("确认审批");
                    alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            leave.remove(position);
                            leaveId.remove(position);
                            leaveListView.setAdapter(leaveAdapter);

                            JSONObject postData = new JSONObject();
                            postData.put("leaveNo", leaveid);
                            postData.put("reviewID", myGlobalData.getEmployeeId());
                            post("/manager/review", postData.toJSONString());
                        }
                    });
                    alertdialogbuilder.setNegativeButton("取消", null);
                    alertdialogbuilder.create().show();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(BranchShowActivity.this);
            builder.setTitle("审批请假");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);

            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

        if (v.getId() == R.id.branch_show_btn_return) {
            Intent intent = new Intent(BranchShowActivity.this, EmployeeShowActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.branch_show_btn_admin) {
            Intent intent = new Intent(BranchShowActivity.this, AdminActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.branch_show_branchdescription) {
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(BranchShowActivity.this);
            alertdialogbuilder.setTitle("描述");
            alertdialogbuilder.setMessage(branchdescriptionTextView.getText().toString().substring(3));
            alertdialogbuilder.setPositiveButton("确定", null);
            alertdialogbuilder.create().show();
        }
    }
}