package com.example.user.smsender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.smsender.models.Komanda;

import java.util.ArrayList;



 public class DBHelper extends SQLiteOpenHelper {
    public static final String KEY_ID = "id";
    public static final String KEY_NOMER = "nomertel";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEXT = "textsms";
    public static final String KEY_DATE = "date";
    public static final String KEY_COLOR = "color";
    private static final String DATABASE_NAME ="myDB";
    private static final String DATABASE_TABLE ="tableComands";
    private static final String DATABASE_TABLE_DATES ="tableDates";

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
                    + KEY_NOMER + " text,"
                    + KEY_NAME + " text,"
                    + KEY_TEXT + " text,"
                    + KEY_DATE +" text,"
                    + KEY_COLOR +" text"
                    + ");");
            db.execSQL("create table " + DATABASE_TABLE_DATES + "("
                    + KEY_ID + " integer primary key autoincrement,"
                    + KEY_DATE +" text,"
                    + KEY_NAME + " text,"
                    + KEY_COLOR + " text"
                    + ");");
            Log.d("MyLogs","Созданы");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        public void addComand (Komanda kom){//TODO:можно брать из муапп исупдейт и тут все движения с добавлением и удалением делать
            ContentValues cv = new ContentValues();
            // подготовим данные для вставки в виде пар: наименование столбца - значение

            //cv.put(KEY_COMID, kom.id);
            cv.put(KEY_NOMER, kom.nomer_tel);
            cv.put(KEY_NAME, kom.name);
            cv.put(KEY_TEXT, kom.text);
            cv.put(KEY_COLOR, kom.color);
            // вставляем запись и получаем ее ID
            long rowID = dbs.insert(DATABASE_TABLE, null, cv);
            Log.d("My_Logs", "row inserted, ID = " + rowID);
            //dbs.close();
        }

    public void addTimestamp (Komanda kom){
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, kom.last_date);
        cv.put(KEY_NAME, kom.name);
        cv.put(KEY_COLOR, kom.color);
        long rowID = dbs.insert(DATABASE_TABLE_DATES, null, cv);
        Log.d("My_Logs", "row inserted, ID = " + rowID + kom.last_date + kom.name);
    }

    public Komanda getComand(int id){
            Komanda komanda = new Komanda();
            String request = "SELECT * FROM " + DATABASE_TABLE +" WHERE " + KEY_ID + " LIKE '" + id + "'";
            //Cursor c = dbs.query(DATABASE_TABLE, new String[] {KEY_COMID, KEY_NOMER, KEY_NAME, KEY_TEXT, KEY_DATE}, id + "=?", new String[] {String.valueOf(id)}, null, null, null, null );
            Cursor c = dbs.rawQuery(request, null);
            if (c != null){
                c.moveToFirst();
                try {
                    if (c.getString(1) != null){
                        //(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                        komanda.id = Integer.parseInt(c.getString(0));
                        komanda.nomer_tel = c.getString(1);
                        komanda.name = c.getString(2);
                        komanda.text = c.getString(3);
                        komanda.last_date = c.getString(4);
                        komanda.color = c.getString(5);
                    }
                } catch (Exception e) {
                    komanda = null;
                    Log.e("Test", e.getMessage(), e);
                }

            }
            c.close();
            return komanda;
    }
    public void delComand (int id){
        dbs.delete(DATABASE_TABLE, KEY_ID + "=" + id,null);
    }

    public void updateComand (Komanda kom){

        ContentValues cv = new ContentValues();
        cv.put(KEY_NOMER, kom.nomer_tel);
        cv.put(KEY_NAME, kom.name);
        cv.put(KEY_TEXT, kom.text);
        cv.put(KEY_DATE, kom.last_date);
        cv.put(KEY_COLOR, kom.color);
        String where = KEY_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(kom.id)};
        int updCount = dbs.update(DATABASE_TABLE, cv, where,
                whereArgs);
        Log.d("MyLogs", updCount + " row updated");
    }

    public ArrayList<Komanda> getallComands (){
        ArrayList <Komanda> listofComands = new ArrayList<>();
        Cursor c = dbs.query(DATABASE_TABLE, null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Komanda komanda = new Komanda();
            komanda.id = Integer.parseInt(c.getString(0));
            komanda.nomer_tel = c.getString(1);
            komanda.name = c.getString(2);
            komanda.text = c.getString(3);
            komanda.last_date = c.getString(4);
            komanda.color = c.getString(5);
            listofComands.add(komanda);
            c.moveToNext();
        }
        return listofComands;
    }

    public ArrayList<Komanda> getallTimestamps (){
        ArrayList <Komanda> listofTimestamps = new ArrayList<>();
        Cursor c = dbs.query(DATABASE_TABLE_DATES, null, null, null, null, null, null);
        c.moveToLast();
        while (!c.isBeforeFirst()) {
            Komanda timestamp = new Komanda();
            timestamp.id = Integer.parseInt(c.getString(0));
            timestamp.last_date = c.getString(1);
            timestamp.name = c.getString(2);
            timestamp.color = c.getString(3);
            listofTimestamps.add(timestamp);
            c.moveToPrevious();
        }
        return listofTimestamps;
    }
    }


