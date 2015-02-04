package com.example.user.smsender;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.smsender.models.Komanda;

import de.greenrobot.event.EventBus;


public class Dialog extends DialogFragment {
    Komanda kom;
    String[] colornames = {"Красный", "Голубой", "Желтый", "Оранжевый", "Зеленый"};
    String[] colors = {"#EF5350","#00B0FF","#FFEB3B","#F57C00","#8BC34A"};
    String color;

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
        // адаптер
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, colornames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Цвет");
        // выделяем элемент
        spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = colors[position];
                Log.d("MyLogs", "color" + color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button yesbtn = (Button) v.findViewById(R.id.btnYes);
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kom = new Komanda();
                kom.id = myApp.currentpos;
                kom.name = nameet.getText().toString();
                kom.text = textet.getText().toString();
                kom.nomer_tel = nomeret.getText().toString();
                if (myApp.isupdate) {
                    kom.last_date = myApp.dbHelper.getComand(myApp.currentpos).last_date;
                }
                kom.color = color;
                if (myApp.isupdate){
                    myApp.dbHelper.updateComand(kom);
                }else {
                    myApp.dbHelper.addComand(kom);
                }
                EventBus.getDefault().post(new RefreshEvent());
                dismiss();
            }
        });
        Button nobtn = (Button) v.findViewById(R.id.btnNo);
        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

