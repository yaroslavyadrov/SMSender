package com.example.user.smsender;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.smsender.models.Command;

import java.util.ArrayList;


public class CommandListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    public ArrayList<Command> objects;
    DBHelper dbHelper;

    CommandListAdapter(Context context) {
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
        Command komanda;
        komanda = objects.get(position);
        return komanda.id;
    }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
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
       if(objects.get(position).lastDate != null) {
        holder.lastusetext.setText("Последняя отправка:" + objects.get(position).lastDate);
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



