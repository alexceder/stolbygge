package se.stolbygge.stolbygge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {
    private PartListAdapter adapter;
    private ArrayList<Part> parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.part_list);

        ListView partListView = (ListView) findViewById(R.id.list_view);

        parts = new ArrayList<>();
        adapter = new PartListAdapter(this, R.layout.part_list_item, parts);

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

        if (id == R.id.action_missing) {
            Intent intent = new Intent(this, MissingPartsActivity.class);

            ArrayList missing = (ArrayList<Part>) parts.clone();
            missing.remove(0);
            missing.remove(0);
            missing.remove(0);
            missing.remove(0);
            missing.remove(0);

            Bundle bundle = new Bundle();
            bundle.putSerializable("missing", missing);
            intent.putExtras(bundle);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
