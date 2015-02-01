package com.example.user.smsender;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    View footer;// = getLayoutInflater().inflate(R.layout.footer, null);
    DialogFragment dialog;




    @ViewById
    ListView kom_list;

    @ItemClick
       void kom_listItemClicked (Komanda selectedKomanda) {
        MyApp myApp = ((MyApp) getApplicationContext());
        myApp.currentpos = selectedKomanda.id;
        showDialog(0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comand_list_activity);
        //DBHelper dbh = new DBHelper(this);
        MyApp myApp = ((MyApp) getApplicationContext());
        //footer = getLayoutInflater().inflate(R.layout.footer, null);
        footer = getLayoutInflater().inflate(R.layout.footer, null, false);


        Button button_footer = (Button) footer.findViewById(R.id.button_footer);
        button_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp myApp = ((MyApp) getApplicationContext());
                myApp.isupdate = false;
                createupdatedial();
                vivspis();
            }
        });
        vivspis();

        //TextView tv = (TextView) findViewById (R.id.textView);
        //ListView kom_list = (ListView) findViewById(R.id.kom_list);

       /* Komanda kom = new Komanda();
        kom.id = 111;
        kom.last_date = "date";
        kom.text = "textsms";
        kom.nomer_tel = "8937";
        kom.name = "name";
        myApp.dbHelper.addComand(kom);
        vivspis();*/


        //textView.setText(kom.name);
        //kom.name = "name1";
        //dbh.updateComand(kom);

        //Komanda kom1 = dbh.getComand(kom.id);
        //dbh.delComand(kom.id);

        //textView.setText(kom1.name);

    }
    void createupdatedial (){
        dialog = new com.example.user.smsender.Dialog();
        dialog.show(getFragmentManager(), "Dialog");
    };

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
                                        //TODO: отправка смс
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Изменить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        MyApp myApp = ((MyApp) getApplicationContext());
                                        myApp.isupdate = true;
                                        createupdatedial();
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
                                        vivspis();
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
        myApp.dbHelper.delComand(myApp.currentpos);
    }
    void vivspis (){
        komListAdapter = new KomListAdapter(this);

        //kom_list.setEmptyView(findViewById(R.id.empty_list_item));
        //View empty = getLayoutInflater().inflate(R.layout.footer, null, false);
        //addContentView(empty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //kom_list.setEmptyView(empty);
        MyApp myApp = ((MyApp) getApplicationContext());

        if (kom_list.getCount() > 0  ){
            if (kom_list.getFooterViewsCount() == 0){
                kom_list.addFooterView(footer);
            }
        } else {
            addContentView(footer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            kom_list.setEmptyView(footer);
        }
        if (kom_list.getCount() != 0){
            kom_list.setAdapter(komListAdapter);
        }




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
