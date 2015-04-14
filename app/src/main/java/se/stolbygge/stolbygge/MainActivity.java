package se.stolbygge.stolbygge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    PartListAdapter partadapter;
    StepListAdapter stepadapter;
    ArrayList<Part> parts;
    ArrayList<Step> steps;
    ListView partListView;
    ListView stepListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //onCreateProductList();

        onCreateStepList();

        /*
        stepListView.post(new Runnable() {
            @Override
            public void run() {
                stepListView.smoothScrollToPosition(4);
            }
        });*/

        //stepListView.smoothScrollToPosition(3);
        //stepListView.smoothScrollBy(600, 1000);


        stepListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int lastVisibleItem = 0;
            private int firstVis = 0;
            private boolean up = false;
            private boolean manual = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Log.d("*** first vis: ",Integer.toString(firstVis));
                if(scrollState == SCROLL_STATE_IDLE && manual) {
                    manual = false;
                    if(firstVis < lastVisibleItem) {
                        Log.d("*** Up", "");
                        lastVisibleItem = firstVis;
                        stepListView.smoothScrollToPosition(lastVisibleItem, 1000);
                    } else {
                        Log.d("*** Down","");
                        lastVisibleItem = firstVis+1;
                        stepListView.smoothScrollToPosition(lastVisibleItem, 1000);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(visibleItemCount == 2) {
                    firstVis = firstVisibleItem+1;
                }
                manual = true;
                Log.d("*** last first ",Integer.toString(firstVisibleItem));
                /*
                Log.d("*** last first ",Integer.toString(firstVisibleItem));
                if(visibleItemCount == 2) {
                    if (firstVisibleItem < lastVisibleItem) { //scroll up
                        //lastVisibleItem = firstVisibleItem;
                        up = true;
                    } else if (firstVisibleItem >= lastVisibleItem) {
                        //lastVisibleItem = firstVisibleItem + 1;
                        up = false;
                    }
               }

               if(up){
                   lastVisibleItem = firstVisibleItem;
               }
                else{
                   lastVisibleItem = firstVisibleItem + 1;
               }*/
            }
        });
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
        partadapter = new PartListAdapter(this,R.layout.part_list_item, parts);

        parts.add(new Part("Insexskruv", "1", "insexskruv", 6));
        parts.add(new Part("Insexnyckel", "1", "insexnyckel", 1));
        parts.add(new Part("Plugg", "1", "plugg", 2));
        parts.add(new Part("Vänster benpar", "1", "vanster_benpar", 1));
        parts.add(new Part("Höger benpar", "1", "hoger_benpar", 1));
        parts.add(new Part("Sitts", "1", "sitts",1));
        parts.add(new Part("Ryggstöd", "1", "ryggstod",1));
        parts.add(new Part("Ryggstödsdekoration", "1", "ryggstodsdekoration", 1));

        partadapter.setParts(parts);
        partListView = (ListView) findViewById(R.id.listview_parts);
        partListView.setAdapter(partadapter);


        return true;
    }

    public boolean onCreateStepList(){

        steps = new ArrayList<Step>();
        parts = new ArrayList<Part>();
        stepadapter = new StepListAdapter(this, R.layout.step_list_item, steps);

        parts.add(new Part("Ryggstöd","1","ryggstod", 1));
        parts.add(new Part("Ryggstödsdekoration","1","ryggstodsdekoration", 1));
        parts.add(new Part("Plugg","1","plugg", 2));
        steps.add(new Step("Steg 1",1,"steg_1", (ArrayList)parts.clone()));
        parts.clear();

        parts.add(new Part("Sits", "1", "sits",1));
        steps.add(new Step("Steg 2",2,"steg_2", (ArrayList)parts.clone()));
        parts.clear();

        parts.add(new Part("Höger benpar","1","hoger_benpar", 1));
        steps.add(new Step("Steg 3",3,"steg_3", (ArrayList)parts.clone()));
        parts.clear();

        parts.add(new Part("Vänster benpar","1","vanster_benpar",1));
        steps.add(new Step("Steg 4",4,"steg_4", (ArrayList)parts.clone()));
        parts.clear();

        parts.add(new Part("Insexskruv", "1", "insexskruv",3));
        steps.add(new Step("Steg 5",5,"steg_5", (ArrayList)parts.clone()));

        steps.add(new Step("Steg 6",6,"steg_6", (ArrayList)parts.clone()));

        stepadapter.setSteps(steps);

        partListView = (ListView) findViewById(R.id.listview_parts);
        partListView.setAdapter(partadapter);

        stepListView = (ListView) findViewById(R.id.listview_steps);
        stepListView.setAdapter(stepadapter);

        return true;
    }
}
