package com.example.user.smsender;


import android.app.DialogFragment;
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

import com.example.user.smsender.models.Command;

import de.greenrobot.event.EventBus;


public class Dialog extends DialogFragment {
    Command kom;
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
        if (myApp.isUpdate){
            getDialog().setTitle("Изменить команду");
            nameet.setText(myApp.dbHelper.getComand(myApp.currentPos).name);
            nomeret.setText(myApp.dbHelper.getComand(myApp.currentPos).phoneNumber);
            textet.setText(myApp.dbHelper.getComand(myApp.currentPos).text);
        } else {
            getDialog().setTitle("Добавить команду");
        }
        // адаптер
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, colornames);
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
                kom = new Command();
                kom.id = myApp.currentPos;
                kom.name = nameet.getText().toString();
                kom.text = textet.getText().toString();
                kom.phoneNumber = nomeret.getText().toString();
                kom.color = color;
                if (myApp.isUpdate){
                    kom.lastDate = myApp.dbHelper.getComand(myApp.currentPos).lastDate;
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
}

