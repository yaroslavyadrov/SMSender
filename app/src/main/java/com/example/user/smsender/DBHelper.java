package com.example.user.smsender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.smsender.models.Komanda;

/**
 * Created by user on 25.01.15.
 */

 public class DBHelper extends SQLiteOpenHelper {
    public static final String KEY_ID = "id";
    public static final String KEY_COMID = "comID";
    public static final String KEY_NOMER = "nomertel";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEXT = "textsms";
    public static final String KEY_DATE = "date";
    private static final String DATABASE_NAME ="myDB";
    private static final String DATABASE_TABLE ="tableComands";

    SQLiteDatabase dbs;
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DATABASE_NAME, null, 1);
            dbs = this.getWritableDatabase();
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            // dbs = getWritableDatabase();
            //  db = dbs;
            //  Log.d("MyLogs", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table " + DATABASE_TABLE + "("
                    + KEY_ID + " integer primary key autoincrement,"
                    + KEY_COMID + " text,"
                    + KEY_NOMER + " text,"
                    + KEY_NAME + " text,"
                    + KEY_TEXT + " text,"
                    + KEY_DATE +" text"
                    + ");");
            Log.d("MyLogs","Созданы");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void addComand (Komanda kom){
            ContentValues cv = new ContentValues();
            // подготовим данные для вставки в виде пар: наименование столбца - значение

            cv.put(KEY_COMID, kom.id);
            cv.put(KEY_NOMER, kom.nomer_tel);
            cv.put(KEY_NAME, kom.name);
            cv.put(KEY_TEXT, kom.text);
            cv.put(KEY_DATE, kom.last_date);
            // вставляем запись и получаем ее ID
            long rowID = dbs.insert("tableComands", null, cv);
            Log.d("My_Logs", "row inserted, ID = " + rowID);
            //dbs.close();
        }


        public Komanda getComand(int id){
            Cursor c = dbs.query("tableComands", new String[] {KEY_COMID, KEY_NOMER, KEY_NAME, KEY_TEXT, KEY_DATE}, id + "=?", new String[] {String.valueOf(id)}, null, null, null, null );
            if (c != null){
                c.moveToFirst();
            }
            Komanda komanda = new Komanda();//(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            komanda.id = Integer.parseInt(c.getString(1));
            komanda.nomer_tel = c.getString(1);
            komanda.name = c.getString(2);
            komanda.text = c.getString(3);
            komanda.last_date = c.getString(4);
            c.close();
            return komanda;
        }

        //public void delComand (int id){

        //}







/*
        public void setTest(int id, String date, String JSON){
            //delete по id надо добавить
            String idS = Integer.toString(id);
            int del = dbs.delete("tableTests", "nameID = " + Integer.toString(id), null);
            //  int del = dbs.delete("tableTests", "nameid = ?",  new String[] {idS});
            ContentValues cv=new ContentValues();
            cv.put("nameID", Integer.toString(id));
            cv.put("date", date);
            cv.put("JSON", JSON);
            dbs.insert("tableTests", null, cv);
        }

        public void setAnswerInBD(int id, String json){
            //delete по id надо добавить
            dbs.delete("tableAnswers", "nameID = " + Integer.toString(id), null);
            ContentValues cv=new ContentValues();
            cv.put("nameID", Integer.toString(id));
            cv.put("JSON", json);
            dbs.insert("tableAnswers", null, cv);
            // Log.d("MyLogs","Добавлен");
        }

        public String getAnswersFromBD(int id){
            String nameID = Integer.toString(id);
            String json;
            Cursor c = dbs.query("tableAnswers",  null, "nameID = ?", new String[] {nameID}, null, null, null);
            c.moveToFirst();
            json = c.getString(2);
            // Log.d("MyLogs","Взят");
            return json;
        }*/
    }


