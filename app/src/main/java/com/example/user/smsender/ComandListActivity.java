package com.example.user.smsender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.smsender.models.Komanda;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

@EActivity
public class ComandListActivity extends ActionBarActivity {
    KomListAdapter komListAdapter;
    View footer;
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
        EventBus.getDefault().post(new RefreshEvent());
    }

    void createupdatedial (){
        dialog = new com.example.user.smsender.Dialog();
        dialog.show(getFragmentManager(), "Dialog");
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final MyApp myApp = ((MyApp) getApplicationContext());
        switch (id) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Выберите действие:")
                        .setCancelable(true)
                        .setPositiveButton("Отправить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        otprdial();
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Изменить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //MyApp myApp = ((MyApp) getApplicationContext());
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
                                        EventBus.getDefault().post(new RefreshEvent());
                                    }
                                })
                        .setNegativeButton("Нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                return builder1.create();
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Хотите отправить команду на номер: " + myApp.dbHelper.getComand(myApp.currentpos).nomer_tel)
                        .setCancelable(true)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Komanda kom;
                                        kom = myApp.dbHelper.getComand(myApp.currentpos);
                                        sendSMS(kom.nomer_tel, kom.text);
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                return builder2.create();
            default:
                return null;
        }
    }

    void showd(){
        showDialog(1);
    }

    void otprdial(){
        showDialog(2);
    }

    void delcomand(){
        MyApp myApp = ((MyApp) getApplicationContext());
        myApp.dbHelper.delComand(myApp.currentpos);
    }

    void updatecurtime (){
        MyApp myApp = ((MyApp) getApplicationContext());
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String s = formatter.format(now);
        Komanda kom;
        kom = myApp.dbHelper.getComand(myApp.currentpos);
        kom.last_date = s;
        myApp.dbHelper.updateComand(kom);
        myApp.dbHelper.addTimestamp(kom);
        Log.d("MyLogs", "now " + s);
        EventBus.getDefault().post(new RefreshEvent());
    }

    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        updatecurtime();
                        Toast.makeText(getBaseContext(), "SMS отправлено",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS доставлено",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS не доставлено",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
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
        if (id == R.id.action_log) {
            LogActivity_.intent(this).start();
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
