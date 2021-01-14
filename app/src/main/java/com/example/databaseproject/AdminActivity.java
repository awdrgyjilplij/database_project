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
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity
        implements View.OnClickListener {

    List<String> branch = new ArrayList<>();
    List<Integer> branchId = new ArrayList<>();

    ListView branchListView;

    Button addbranchButton;
    Button searchButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        String extra_data = intent.getStringExtra("extra_data");
        if (extra_data != null) {
            Toast toast = Toast.makeText(AdminActivity.this,
                    extra_data, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        addbranchButton = findViewById(R.id.admin_btn_addbranch);
        searchButton = findViewById(R.id.admin_btn_search);
        logoutButton = findViewById(R.id.admin_btn_logout);
        addbranchButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        branchListView = findViewById(R.id.admin_branchinfo);

        get("/dept", adminInfoRecall);

        branchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer branchid = branchId.get(position);
                Logger.d("branchidfromadminfirsttime", branchid.toString());
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(AdminActivity.this);
                alertdialogbuilder.setMessage(branch.get(position));

                alertdialogbuilder.setPositiveButton("前往部门", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logger.d("branchidfromadmin", branchid.toString());
                        Intent intent = new Intent(AdminActivity.this, BranchShowActivity.class);
                        intent.putExtra("extra_data", "welcome administrator");
                        intent.putExtra("extra_id", branchid);
                        startActivity(intent);
                    }
                });

                alertdialogbuilder.setNeutralButton("删除部门", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(AdminActivity.this);
                        alertdialogbuilder.setMessage("确认删除");
                        alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                branch.remove(position);
                                branchId.remove(position);
                                ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(AdminActivity.this,
                                        android.R.layout.simple_list_item_1, branch.toArray(new String[0]));
                                branchListView.setAdapter(branchAdapter);

                                JSONObject postData = new JSONObject();
                                postData.put("deptID", branchid);
                                post("/admin/deleteDept", postData.toJSONString());
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
    }

    private final RecallFunction adminInfoRecall = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);

        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = ("" + obj.getInteger("deptID")) + " " + obj.getString("deptName");
            branch.add(item);
            branchId.add(obj.getInteger("deptID"));
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(AdminActivity.this,
                    android.R.layout.simple_list_item_1, branch.toArray(new String[0]));
            branchListView.setAdapter(branchAdapter);
        });
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.admin_btn_addbranch) {

            View view = View.inflate(AdminActivity.this, R.layout.branch_changeinfo_view, null);

            EditText nameText, descriptionText;
            nameText = view.findViewById(R.id.branch_changeinfo_name);
            descriptionText = view.findViewById(R.id.branch_changeinfo_description);

            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            builder.setTitle("增加部门");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    JSONObject postData = new JSONObject();
                    postData.put("deptName", nameText.getText().toString());
                    postData.put("description", descriptionText.getText().toString());
                    post("/admin/addDept", postData.toJSONString());
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }

        if (v.getId() == R.id.admin_btn_search) {
            Intent intent = new Intent(AdminActivity.this, SearchActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.admin_btn_logout) {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}