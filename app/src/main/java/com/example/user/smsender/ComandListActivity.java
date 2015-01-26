package com.example.user.smsender;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.smsender.models.Komanda;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class ComandListActivity extends ActionBarActivity {

    @ViewById
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comand_list_activity);



        //TextView tv = (TextView) findViewById (R.id.textView);

        Komanda kom = new Komanda();
        kom.id = 111;
        kom.last_date = "date";
        kom.text = "textsms";
        kom.nomer_tel = "8937";
        kom.name = "name";
        DBHelper dbh = new DBHelper(this);
        dbh.addComand(kom);
        Komanda kom1 = dbh.getComand(222);
        dbh.delComand(kom.id);

        //textView.setText(kom1.text);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
