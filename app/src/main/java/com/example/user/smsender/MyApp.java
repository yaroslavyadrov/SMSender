package com.example.user.smsender;

import android.app.Application;


public class MyApp extends Application {
    public int currentPos;
    public boolean isUpdate, isClick = true;
    DBHelper dbHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
    }
}
