package com.example.user.smsender;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.smsender.models.Komanda;

import de.greenrobot.event.EventBus;


public class Dialog extends DialogFragment {
    Komanda kom;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.dialog_cu, null);
        final MyApp myApp = ((MyApp) getActivity().getApplication());
        final EditText nameet = (EditText) v.findViewById(R.id.name_et);
        final EditText nomeret = (EditText) v.findViewById(R.id.nomer_et);
        final EditText textet = (EditText) v.findViewById(R.id.text_et);
        if (myApp.isupdate){
            getDialog().setTitle("Изменить команду");
            nameet.setText(myApp.dbHelper.getComand(myApp.currentpos).name);
            nomeret.setText(myApp.dbHelper.getComand(myApp.currentpos).nomer_tel);
            textet.setText(myApp.dbHelper.getComand(myApp.currentpos).text);
        } else {
            getDialog().setTitle("Добавить команду");
        }
        Button yesbtn = (Button) v.findViewById(R.id.btnYes);
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kom = new Komanda();
                kom.name = nameet.getText().toString();
                kom.text = textet.getText().toString();
                kom.nomer_tel = nomeret.getText().toString();
                if (myApp.isupdate){
                    myApp.dbHelper.updateComand(kom);
                }else {
                    myApp.dbHelper.addComand(kom);
                }
                EventBus.getDefault().post(new RefreshEvent());
                dismiss();
            }
        });
        return v;
    }


    public void onDismiss(DialogInterface dialog) {
        //EventBus.getDefault().unregister(this);
        super.onDismiss(dialog);
        //Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        //EventBus.getDefault().unregister(this);
        super.onCancel(dialog);
        //Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}

