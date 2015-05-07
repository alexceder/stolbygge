package se.stolbygge.stolbygge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class InstructionsActivity extends Activity {

    StepListAdapter stepAdapter;
    ArrayList<Part> parts;
    ArrayList<Step> steps;
    ListView stepListView;
    boolean fromClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_activity);

        onCreateStepList();

        stepListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int lastVisibleItem = 0;
            private int firstVis = 0;
            private boolean manual = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                // Should only be computed if the action is from user scroll, not buttons
                if(fromClick) {

                    // Snap view to the next item in the scroll direction
                    if(scrollState == SCROLL_STATE_IDLE && manual) {
                        if(firstVis < lastVisibleItem) { // Up
                            lastVisibleItem = firstVis;
                            stepListView.smoothScrollToPosition(lastVisibleItem, 1000);
                            manual = false;
                        } else { // Down
                            lastVisibleItem = firstVis+1;
                            stepListView.smoothScrollToPosition(lastVisibleItem, 1000);
                            manual = false;
                        }
                    }
                    fromClick = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Keep track of where the scroll is in order to know where to snap
                if(visibleItemCount == 2) {
                    firstVis = firstVisibleItem;
                    manual = true;
                }
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

    public boolean onCreateStepList() {
        Store store = Store.getInstance();
        stepAdapter = new StepListAdapter(this, R.layout.step_list_item, store.getSteps());

        stepListView = (ListView) findViewById(R.id.listview_steps);
        stepListView.setAdapter(stepAdapter);

        // Create a list of navigation buttons
        LinearLayout buttonList = (LinearLayout) findViewById(R.id.button_list);
        for(int i = 0; i < steps.size(); i++) {
            final Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            button.setId(i);
            button.setBackgroundColor(getResources().getColor(R.color.bgc_progressbar_unvisited));
            button.setText(Integer.toString(i+1));
            button.setLayoutParams(params);

            // Clicking on one of the buttons redirect to that step
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollTo(button.getId());
                }
            });
            buttonList.addView(button);
        }
        return true;
    }

    public void scrollTo(int position) {
        fromClick = true;
        stepListView.smoothScrollToPosition(position);
    }
}
