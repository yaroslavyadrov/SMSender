package com.example.user.smsender;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.user.smsender.models.Komanda;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EActivity
public class ComandListActivity extends ActionBarActivity {
    KomListAdapter komListAdapter;
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

    public void onEvent (RefreshEvent e){
        komListAdapter = new KomListAdapter(this);
        MyApp myApp = ((MyApp) getApplicationContext());
        if (kom_list.getFooterViewsCount() == 0){
            kom_list.addFooterView(footer);
        }
        if (myApp.dbHelper.getallComands() != null){
            kom_list.setAdapter(komListAdapter);

        } else {
            addContentView(footer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            kom_list.setEmptyView(footer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comand_list_activity);
        EventBus.getDefault().register(this);
        footer = getLayoutInflater().inflate(R.layout.footer, null);

        Button button_footer = (Button) footer.findViewById(R.id.button_footer);
        button_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp myApp = ((MyApp) getApplicationContext());
                myApp.isupdate = false;
                createupdatedial();
            }
        });
        EventBus.getDefault().post(new RefreshEvent());;


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
                                        delcomand();
                                        EventBus.getDefault().post(new RefreshEvent());;
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
