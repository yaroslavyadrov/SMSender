package com.example.user.smsender;

import android.app.Application;


public class MyApp extends Application {
    public int currentpos;
    public boolean isupdate;
    DBHelper dbHelper;
    @Override
    public void onCreate(){

        dbHelper = new DBHelper(this);
    }
}
