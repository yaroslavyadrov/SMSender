package com.example.user.smsender;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.smsender.models.Komanda;


public class Dialog extends DialogFragment implements View.OnClickListener{
    Komanda kom;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.dialog_cu, null);
        MyApp myApp = ((MyApp) getActivity().getApplication());
        EditText nameet = (EditText) v.findViewById(R.id.name_et);
        EditText nomeret = (EditText) v.findViewById(R.id.nomer_et);
        EditText textet = (EditText) v.findViewById(R.id.text_et);
        kom = new Komanda();
        if (myApp.isupdate){
            getDialog().setTitle("Изменить команду");
            nameet.setText(myApp.dbHelper.getComand(myApp.currentpos).name);
            nomeret.setText(myApp.dbHelper.getComand(myApp.currentpos).nomer_tel);
            textet.setText(myApp.dbHelper.getComand(myApp.currentpos).text);
        } else {
            getDialog().setTitle("Добавить команду");
        }
        kom.name = nameet.getText().toString();
        kom.nomer_tel = nomeret.getText().toString();
        kom.text = textet.getText().toString();
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        v.findViewById(R.id.btnNo).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        //Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());

        switch (v.getId()) {
            case R.id.btnYes:
                MyApp myApp = ((MyApp) getActivity().getApplication());
                if (myApp.isupdate){
                    myApp.dbHelper.updateComand(kom);
                }else {
                    myApp.dbHelper.addComand(kom);
                }
                Log.d("MyLogs", "da");
                break;
            case R.id.btnNo:
                Log.d("MyLogs", "da");
                break;
            default:
                break;
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}

