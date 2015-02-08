package com.example.user.smsender;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.smsender.models.Komanda;

import java.util.ArrayList;


public class KomListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    public ArrayList<Komanda> objects;
    DBHelper dbHelper;

    KomListAdapter(Context context) {
        ctx = context;
        dbHelper = new DBHelper(context);
        objects = dbHelper.getallComands();
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return objects!=null ? objects.size() : 0;
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

   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = lInflater.inflate(R.layout.kom_list_item, parent, false);
        }
        else {
            view = convertView;
        }
        Komanda p = objects.get(position);
        if (p.last_date != null) {
            ((TextView) view.findViewById(R.id.kom_lastuse_tv)).setText("Последний раз отсылалось: " + p.last_date);
            //p.last_date = null;
        }
        int col = Color.parseColor(p.color);
        ((TextView) view.findViewById(R.id.kom_name_tv)).setText(p.name);
        view.findViewById(R.id.linlayBack).setBackgroundColor(col);
        return view;
    }
}*/
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       //System.out.println("getView " + position + " " + convertView);
       ViewHolder holder;
       if (convertView == null) {
           holder = new ViewHolder();

       } else {
           holder = (ViewHolder)convertView.getTag();
       }
       convertView = lInflater.inflate(R.layout.kom_list_item, null);
       holder.nametext = (TextView)convertView.findViewById(R.id.kom_name_tv);
       holder.lastusetext = (TextView)convertView.findViewById(R.id.kom_lastuse_tv);
       holder.linlayback = (LinearLayout)convertView.findViewById(R.id.linlayBack);
       convertView.setTag(holder);

       holder.nametext.setText(objects.get(position).name);
       if(objects.get(position).last_date != null) {
        holder.lastusetext.setText(objects.get(position).last_date);
       }
       int col = Color.parseColor(objects.get(position).color);
       holder.linlayback.setBackgroundColor(col);
       return convertView;
   }

}

class ViewHolder {
    public TextView nametext, lastusetext;
    public LinearLayout linlayback;
}



