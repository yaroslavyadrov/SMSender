package com.example.user.smsender;

import android.Manifest;
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

import com.example.user.smsender.models.Command;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import rx.functions.Action1;

@EActivity
public class CommandListActivity extends ActionBarActivity {
    CommandListAdapter komListAdapter;
    View footer;
    DialogFragment dialog;
    Boolean oneSend;

    @ViewById
    ListView kom_list;

    @AfterViews
    void init() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.SEND_SMS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            finish();
                        }
                    }
                });
    }

    @ItemClick
    void kom_listItemClicked(Command selectedKomanda) {
        MyApp myApp = ((MyApp) getApplicationContext());
        if (myApp.isClick) {
            myApp.currentPos = selectedKomanda.id;
            showDialog(0);
        } else {
            Toast.makeText(getBaseContext(), "Дождитесь отправки сообщения",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void onEvent(LogEvent event) {
        MyApp myApp = ((MyApp) getApplicationContext());
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String s = formatter.format(now);
        Command kom;
        kom = myApp.dbHelper.getComand(myApp.currentPos);
        kom.lastDate = s;
        myApp.dbHelper.updateComand(kom);
        myApp.dbHelper.addTimestamp(kom);
        Log.d("MyLogs", "now " + s);
        myApp.isClick = true;
        //kom_list.setClickable(true);
        EventBus.getDefault().post(new RefreshEvent());
    }

    public void onEventAsync(SendEvent send) {
        final MyApp myApp = ((MyApp) getApplicationContext());
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (oneSend) {
                            EventBus.getDefault().post(new LogEvent());
                            //updatecurtime();
                            oneSend = false;
                        }
                        Toast.makeText(getBaseContext(), "SMS отправлено",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        myApp.isClick = true;
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        myApp.isClick = true;
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        myApp.isClick = true;
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        myApp.isClick = true;
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
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
        sms.sendTextMessage(send.phoneNumber, null, send.message, sentPI, deliveredPI);
    }

    public void onEvent(RefreshEvent e) {
        komListAdapter = new CommandListAdapter(this);
        MyApp myApp = ((MyApp) getApplicationContext());
        if (kom_list.getFooterViewsCount() == 0) {
            kom_list.addFooterView(footer);
        }
        if (myApp.dbHelper.getallComands() != null) {
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
                myApp.isUpdate = false;
                createupdatedial();
            }
        });
        EventBus.getDefault().post(new RefreshEvent());
    }

    void createupdatedial() {
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
                                        myApp.isUpdate = true;
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
                builder2.setMessage("Отправить команду?")
                        .setCancelable(true)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        oneSend = true;
                                        myApp.isClick = false;
                                        Command kom;
                                        kom = myApp.dbHelper.getComand(myApp.currentPos);
                                        EventBus.getDefault().post(new SendEvent(kom.phoneNumber, kom.text));
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

    void showd() {
        showDialog(1);
    }

    void otprdial() {
        showDialog(2);
    }

    void delcomand() {
        MyApp myApp = ((MyApp) getApplicationContext());
        myApp.dbHelper.delComand(myApp.currentPos);
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
