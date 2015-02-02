package com.example.user.smsender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.smsender.models.Komanda;

import java.util.ArrayList;

/**
 * Created by user on 26.01.15.
 */
public class KomListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    public ArrayList<Komanda> objects;
    DBHelper dbHelper;

    KomListAdapter(Context context){
        ctx = context;
        dbHelper = new DBHelper(context);
        objects = dbHelper.getallComands();
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        Komanda komanda;
        komanda = objects.get(position);
        return komanda.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.kom_list_item, parent, false);
        }

        Komanda p = objects.get(position);
        //if (p.last_date != null){//TODO:раскоментить строку, потому что щас не показывает тк дата пустая
            ((TextView) view.findViewById(R.id.kom_lastuse_tv)).setText(p.id+" "+p.last_date+" "+p.nomer_tel);
        //}
        ((TextView) view.findViewById(R.id.kom_name_tv)).setText(p.name+p.text);
        return view;
    }


}
