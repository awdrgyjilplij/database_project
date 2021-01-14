package com.example.databaseproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseActivity {

    List<String> result = new ArrayList<>();
    ListView resultListView;
    private final String[] data = {"指定月请假和迟到前十名", "审核请假数量最多经理的请假情况", "指定部门扣发工资不低于公司平均实发工资的员工",
            "指定部门指定月实发工资最高的员工", "指定月迟到总时长>=部门平均因公请假时长的员工",
            "指定月迟到超过两次且因私请假超过两次的员工", "查询指定员工XML文档"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button ret_button = findViewById(R.id.search_btn_return);
        ret_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, data);
        ListView searchListView = findViewById(R.id.search_listview);
        resultListView = findViewById(R.id.search_result);

        result.add("无");
        ArrayAdapter<String> resultAdapter_ = new ArrayAdapter<>(SearchActivity.this,
                android.R.layout.simple_list_item_1, result.toArray(new String[0]));
        resultListView.setAdapter(resultAdapter_);

        searchListView.setAdapter(searchAdapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_, int position, long id) {
                if (position == 0) {//指定月请假和迟到前十名
                    View view = View.inflate(SearchActivity.this, R.layout.search_month_view, null);

                    EditText monthText;
                    monthText = view.findViewById(R.id.search_month_month);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定月份");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("period", monthText.getText().toString());
                            post("/search/lateAndLeaveMost", postData.toJSONString(), recallZero);
                            alertDialog.dismiss();
                        }
                    });
                }

                if (position == 1) {//审核请假数量最多经理的情况
                    get("/search/greatestManager/leave", recallOne);
                }

                if (position == 2) {//指定部门扣发工资不低于公司平均实发工资的员工
                    View view = View.inflate(SearchActivity.this, R.layout.search_branch_view, null);

                    EditText branchText;
                    branchText = view.findViewById(R.id.search_branch_branch);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定部门");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("deptID", branchText.getText().toString());
                            post("/search/deduceMostEmployee", postData.toJSONString(), recallZero);
                            alertDialog.dismiss();
                        }
                    });
                }

                if (position == 3) {//指定部门指定月实发工资最高的员工
                    View view = View.inflate(SearchActivity.this, R.layout.search_monthandbranch_view, null);

                    EditText monthText, branchText;
                    monthText = view.findViewById(R.id.search_monthandbranch_month);
                    branchText = view.findViewById(R.id.search_monthandbranch_branch);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定月份和部门");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("deptID", branchText.getText().toString());
                            postData.put("period", monthText.getText().toString());
                            post("/search/mostPayEmployee", postData.toJSONString(), recallTwo);
                            alertDialog.dismiss();
                        }
                    });
                }

                if (position == 4) {//指定月迟到总时长>=部门平均因公请假时长的员工
                    View view = View.inflate(SearchActivity.this, R.layout.search_month_view, null);

                    EditText monthText;
                    monthText = view.findViewById(R.id.search_month_month);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定月份");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("period", monthText.getText().toString());
                            post("/search/lateMuchEmployee", postData.toJSONString(), recallZero);
                            alertDialog.dismiss();
                        }
                    });
                }

                if (position == 5) {//指定月迟到超过两次且因私请假超过两次的员工和部门、经理
                    View view = View.inflate(SearchActivity.this, R.layout.search_month_view, null);

                    EditText monthText;
                    monthText = view.findViewById(R.id.search_month_month);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定月份");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("period", monthText.getText().toString());
                            post("/search/lateAndLeaveMuchEmployee", postData.toJSONString(), recallZero);
                            alertDialog.dismiss();
                        }
                    });
                }

                if (position == 6) {//查询指定员工XML文档
                    View view = View.inflate(SearchActivity.this, R.layout.search_employeexml_view, null);

                    EditText employeeText;
                    employeeText = view.findViewById(R.id.search_employeexml_employee);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("指定员工");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setView(view);
                    builder.setPositiveButton("提交查询", null);
                    builder.setNeutralButton("取消", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject postData = new JSONObject();
                            postData.put("name", employeeText.getText().toString());
                            post("/xml", postData.toJSONString(), recallThree);
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private final RecallFunction recallZero = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        result.clear();
        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = ("" + obj.getInteger("employeeID")) + " " + obj.getString("name");
            Logger.d("item", item);
            result.add(item);
        }
        if (result.size() == 0)
            result.add("无");
        runOnUiThread(() -> {
            ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, result.toArray(new String[0]));
            resultListView.setAdapter(resultAdapter);
        });
    };
    private final RecallFunction recallOne = (s) -> {
        JSONArray jsonArray = JSONArray.parseArray(s);
        result.clear();
        for (Object o : jsonArray) {
            JSONObject obj = (JSONObject) o;
            String item = obj.getString("leaveBegin") + "-" + obj.getString("leaveEnd") + " " + obj.getString("leaveReason");
            result.add(item);
        }
        if (result.size() == 0)
            result.add("无");
        runOnUiThread(() -> {
            ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, result.toArray(new String[0]));
            resultListView.setAdapter(resultAdapter);
        });
    };
    private final RecallFunction recallTwo = (s) -> {
        JSONObject jsonParser = JSONObject.parseObject(s);
        result.clear();
        String item = ("" + jsonParser.getInteger("employeeID")) + " " + jsonParser.getString("name");
        result.add(item);
        if (result.size() == 0)
            result.add("无");
        runOnUiThread(() -> {
            ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, result.toArray(new String[0]));
            resultListView.setAdapter(resultAdapter);
        });
    };
    private final RecallFunction recallThree = (s) -> {
        //TODO split
        result.clear();
        Logger.d("xml", s);
        String[] items = s.split("\r");
        result.addAll(Arrays.asList(items));
        if (result.size() == 0)
            result.add("无");
        runOnUiThread(() -> {
            ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, result.toArray(new String[0]));
            resultListView.setAdapter(resultAdapter);
        });
    };
}