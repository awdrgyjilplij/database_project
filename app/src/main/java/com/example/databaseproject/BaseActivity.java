package com.example.databaseproject;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private ServerConnector connector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = new ServerConnector();
    }

    public void post(String path, String output, RecallFunction function) {
        connector.post(path, output, function);
    }

    public void post(String path, String output) {
        post(path, output, null);
    }

    public void get(String path, RecallFunction function) {
        connector.get(path, function);
    }
}