package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.databaseproject.ui.main.SectionsPagerAdapter;

public class EmployeeShowActivity extends BaseActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_show);

        Button changeinfoButton=findViewById(R.id.btn_changeinfo);
        Button checkinButton=findViewById(R.id.btn_checkin);
        Button leaveButton=findViewById(R.id.btn_leave);
        changeinfoButton.setOnClickListener(this);
        checkinButton.setOnClickListener(this);
        leaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_changeinfo) {
            Intent intent = new Intent(EmployeeShowActivity.this, EmployeeSetActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_checkin) {
            Intent intent = new Intent(EmployeeShowActivity.this, EmployeeSetActivity.class);
            startActivity(intent);
            //TODO: checkin
        }
        if (v.getId() == R.id.btn_leave) {
            Intent intent = new Intent(EmployeeShowActivity.this, EmployeeSetActivity.class);
            startActivity(intent);
            //TODO: leave
        }
    }
}
