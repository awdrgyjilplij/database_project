package com.example.databaseproject;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

public abstract class BaseActivity extends AppCompatActivity {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "rine.db";

    DBHelper dbHelper;

    private ServerConnector connector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = new ServerConnector();

        dbHelper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
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

    public String string2md5(String rawString) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] rawBytes = StringByte.stringToBytes(rawString, "utf-8");
        if (rawBytes == null) {
            Logger.e("RINE_ERROR", "null rawBytes");
            return "";
        }
        byte[] md5Bytes = md5.digest(rawBytes);
        StringBuilder hexValue = new StringBuilder();
        for (byte b: md5Bytes) {
            int val = ((int) b) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}