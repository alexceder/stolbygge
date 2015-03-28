package se.stolbygge.stolbygge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    PartListAdapter adapter;
    ArrayList<Part> parts;
    ListView partListView;
    GridView partGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onCreateProductList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public boolean onCreateProductList(){

        parts = new ArrayList<Part>();
        adapter = new PartListAdapter(this,R.layout.part_list_item, parts);

        parts.add(new Part("Insexskruv", "1", "insexskruv",6));
        parts.add(new Part("Insexnyckel", "1", "insexnyckel", 1));
        parts.add(new Part("Plugg","1","plugg",2));
        parts.add(new Part("Vänster benpar","1","vanster_benpar",1));
        parts.add(new Part("Höger benpar","1","hoger_benpar",1));
        parts.add(new Part("Sitts","1","sitts",1));
        parts.add(new Part("Ryggstöd","1","ryggstod",1));
        parts.add(new Part("Ryggstödsdekoration","1","ryggstodsdekoration",1));


        adapter.setParts(parts);
        partListView = (ListView) findViewById(R.id.listview);
        partGridView = (GridView) findViewById(R.id.gridview);
        partGridView.setNumColumns(parts.size());
        partGridView.setColumnWidth(300);
        //partGridView.setAdapter(adapter);
        partListView.setAdapter(adapter);

        return true;
    }
}
