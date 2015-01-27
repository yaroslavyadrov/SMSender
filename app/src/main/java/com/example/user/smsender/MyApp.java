package com.example.user.smsender;

import android.app.Application;

/**
 * Created by user on 27.01.15.
 */
public class MyApp extends Application {
    DBHelper dbHelper;
    @Override
    public void onCreate(){

        dbHelper = new DBHelper(this);
    }
}
