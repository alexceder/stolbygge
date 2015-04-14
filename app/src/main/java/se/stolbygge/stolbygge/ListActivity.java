package se.stolbygge.stolbygge;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "se.stolbygge.stolbygge.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.part_list);

        PartListAdapter adapter;
        ArrayList<Part> parts;
        ListView partListView;
        TextView logging;
        GridView partGridView;

        partListView = (ListView) findViewById(R.id.listview);

        parts = new ArrayList<Part>();
        adapter = new PartListAdapter(this);

        parts.add(new Part("Insexskruv", "1", "insexskruv",6));
        parts.add(new Part("Insexnyckel", "1", "insexnyckel", 1));
        parts.add(new Part("Plugg","1","plugg",2));
        parts.add(new Part("Vänster benpar","1","vanster_benpar",1));
        parts.add(new Part("Höger benpar","1","hoger_benpar",1));
        parts.add(new Part("Sitts","1","sitts",1));
        parts.add(new Part("Ryggstöd","1","ryggstod",1));
        parts.add(new Part("Ryggstödsdekoration","1","ryggstodsdekoration",1));

        adapter.setParts(parts);

        partListView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
