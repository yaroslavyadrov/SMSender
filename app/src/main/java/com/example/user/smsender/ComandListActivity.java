package com.example.user.smsender;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.smsender.models.Komanda;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity
public class ComandListActivity extends ActionBarActivity {
    KomListAdapter komListAdapter;
    ArrayList<Komanda> komlist;

    @ViewById
    ListView kom_list;
    int posspisok;

    @ItemClick
       void kom_listItemClicked (Komanda selectedKomanda) {
        posspisok = selectedKomanda.id;
        showDialog(0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comand_list_activity);
        //DBHelper dbh = new DBHelper(this);
        MyApp myApp = ((MyApp) getApplicationContext());

        //TextView tv = (TextView) findViewById (R.id.textView);
        //ListView kom_list = (ListView) findViewById(R.id.kom_list);

        Komanda kom = new Komanda();
        kom.id = 111;
        kom.last_date = "date";
        kom.text = "textsms";
        kom.nomer_tel = "8937";
        kom.name = "name";
        myApp.dbHelper.addComand(kom);
        komListAdapter = new KomListAdapter(this);
        kom_list.setAdapter(komListAdapter);



        //textView.setText(kom.name);
        //kom.name = "name1";
        //dbh.updateComand(kom);

        //Komanda kom1 = dbh.getComand(kom.id);
        //dbh.delComand(kom.id);

        //textView.setText(kom1.name);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Выберите действие:")
                        .setCancelable(true)
                        .setPositiveButton("Отправить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Изменить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        showd();
                                        dialog.cancel();
                                    }
                                });

                return builder.create();
            case 1:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Удалить команду?")
                        .setCancelable(true)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //myApp.dbHelper.delComand(posspisok);
                                        delcomand();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        dialog.cancel();

                                    }
                                })
                        .setNegativeButton("Нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder1.create();


            default:
                return null;
        }
    }
    void showd(){
        showDialog(1);
    }
    void delcomand(){
        MyApp myApp = ((MyApp) getApplicationContext());
        myApp.dbHelper.delComand(posspisok);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comand_list_avtivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
