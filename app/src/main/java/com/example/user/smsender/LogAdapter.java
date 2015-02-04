package com.example.user.smsender;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.smsender.models.Komanda;

import java.util.ArrayList;

/**
 * Created by user on 05.02.15.
 */
public class LogAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    public ArrayList<Komanda> objects;
    DBHelper dbHelper;

    LogAdapter(Context context){
        ctx = context;
        dbHelper = new DBHelper(context);
        objects = dbHelper.getallTimestamps();
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
            view = lInflater.inflate(R.layout.log_lv_item, parent, false);
        }

        Komanda p = objects.get(position);

        ((TextView) view.findViewById(R.id.datetv)).setText(p.last_date);

        int col = Color.parseColor(p.color);
        ((TextView) view.findViewById(R.id.nametv)).setText(p.name);
        view.findViewById(R.id.log_itm_back).setBackgroundColor(col);
        return view;
    }
}
