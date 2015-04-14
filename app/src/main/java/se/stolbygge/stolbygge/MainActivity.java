package se.stolbygge.stolbygge;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    /*
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
    */


    // Is called when the button "Product List" with id button1 on the homepage is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // The operation is to call ListActivity.class that shows and handles the product list.
    // Starts the intent.
    public void onCreateProductList(View view){
        Intent I = new Intent(this, se.stolbygge.stolbygge.ListActivity.class);
        //Log.d("******", "i metoden onCreateProductList i MainActivity");
        startActivity(I);
    }

    // Is called when the button "Result List" with id button2 on the homepage is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // The operation is to call ResultList.class that shows and handles the result list.
    // Starts the intent.
    public void onCreateResultList(View view) {
        //Kommentera fram detta för att få funktion till knappen Result List
        //Instead on ListActivitity.class, write in the class for the result page.

        //Intent I = new Intent(this, se.stolbygge.stolbygge.ListActivity.class);
        //startActivity(I);
    }

    // Is called when the button "AR" with id button3 on the homepage is clicked.
    // Creates an abstract description called intent I, with an operation to be performed.
    // TODO Implement
    // The operation is to call AR. Change ....ListActivity.class.
    // Starts the intent.
    public void onCreateAR(View view) {
        //Kommentera fram detta för att få funktion till knappen AR
        //Instead on ListActivitity.class, write in the class for the AR page.

        //Intent I = new Intent(this, se.stolbygge.stolbygge.ListActivity.class);
        //startActivity(I);
    }
}
