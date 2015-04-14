package se.stolbygge.stolbygge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class MissingPartsActivity extends ActionBarActivity {

    MissingListAdapter adapter;
    ArrayList<Part> missing;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_missing_parts);

        setupMissingList();
    }

    private void setupMissingList() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            missing = (ArrayList<Part>) extras.getSerializable("missing");
            adapter = new MissingListAdapter(this, R.layout.missing_list_item, missing);
            listView = (ListView) findViewById(R.id.missing_list_view);
            listView.setAdapter(adapter);

            setTitle(getString(R.string.title_activity_parts) + " (" + adapter.getCount() + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parts, menu);
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
