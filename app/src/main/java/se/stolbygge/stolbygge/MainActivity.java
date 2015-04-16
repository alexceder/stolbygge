package se.stolbygge.stolbygge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    // Is called when the button "Product List" is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // The operation is to call ListActivity.class that shows and handles the product list.
    // Starts the intent.
    public void onCreateProductList(View view) {
        Intent intent = new Intent(this, se.stolbygge.stolbygge.ListActivity.class);
        startActivity(intent);
    }

    // Is called when the button "Result List" is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // Starts the intent.
    public void onCreateInstructionList(View view) {//TODO implement when ready
        Intent intent = new Intent(this, se.stolbygge.stolbygge.InstructionsActivity.class);
        startActivity(intent);
    }

    // Is called when the button "Result List" is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // Starts the intent.
    public void onCreateResultList(View view) { //TODO implement when ready
        //Intent intent = new Intent(this, se.stolbygge.stolbygge.ListActivity.class);
        //startActivity(intent);
    }

    // Is called when the button "AR" is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // Starts the intent.
    public void onCreateAR(View view) { //TODO implement when ready
        Intent intent = new Intent(this, ARActivity.class);
        startActivity(intent);
    }
}
