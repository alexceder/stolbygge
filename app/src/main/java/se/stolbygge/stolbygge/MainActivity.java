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
    StepListAdapter stepadapter;
    ArrayList<Part> parts;
    ArrayList<Step> steps;
    ListView partListView;
    GridView partGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //onCreateProductList();

        onCreateStepList();
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

    public boolean onCreateStepList(){

        steps = new ArrayList<Step>();
        parts = new ArrayList<Part>();
        stepadapter = new StepListAdapter(this, R.layout.step_list_item, steps);

        parts.add(new Part("Ryggstöd","1","ryggstod", 1));
        parts.add(new Part("Ryggstödsdekoration","1","ryggstodsdekoration", 1));
        parts.add(new Part("Plugg","1","plugg", 2));
        steps.add(new Step("Steg 1",1,"steg_1", parts));
        parts.clear();

        parts.add(new Part("Sits", "1", "sits",1));
        steps.add(new Step("Steg 2",2,"steg_2", parts));
        parts.clear();

        parts.add(new Part("Höger benpar","1","hoger_benpar", 1));
        steps.add(new Step("Steg 3",3,"steg_3", parts));
        parts.clear();

        parts.add(new Part("Vänster benpar","1","vanster_benpar",1));
        steps.add(new Step("Steg 4",4,"steg_4", parts));
        parts.clear();

        parts.add(new Part("Insexskruv", "1", "insexskruv",3));
        steps.add(new Step("Steg 5",5,"steg_5", parts));

        steps.add(new Step("Steg 6",6,"steg_6", parts));

        stepadapter.setSteps(steps);
        partListView = (ListView) findViewById(R.id.listview);
        partGridView = (GridView) findViewById(R.id.gridview);
        partGridView.setNumColumns(steps.size());
        partGridView.setColumnWidth(300);
        //partGridView.setAdapter(adapter);
        partListView.setAdapter(stepadapter);


        return true;
    }
}
