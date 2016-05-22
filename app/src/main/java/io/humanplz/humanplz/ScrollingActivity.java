package io.humanplz.humanplz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ScrollingActivity extends AppCompatActivity {

    private static final Logger log = Logger.getLogger( ScrollingActivity.class.getName() );

    private ListView listView;

    private ArrayList<DataEntry> dataEntries;

    private URLDataDownload URLDataDownload = new URLDataDownload();
    private final String DATABASE_FILE_URL = "https://raw.githubusercontent.com/PIsberg/humanplz/master/humanplz.db";

    final Intent intentCall = new Intent("android.intent.action.CALL");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        IntentFilter filter = new IntentFilter("io.humanplz.humanplz.PhoneCallReceiver");
        this.registerReceiver(new PhoneCallReceiver(), filter);

        Toast.makeText(this, "Enabled PhoneCallReceiver", Toast.LENGTH_SHORT)
                .show();

        log.info("Enabled PhoneCallReceiver");

        if(dataEntries == null) {
            URLDataDownload.execute(DATABASE_FILE_URL);
        }


        // NOTE: this is temporary hack.. to be replaced with a real database
        while(!URLDataDownload.isFinishedDownloading());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentCall);


            }
        });

        listView = (ListView) findViewById(R.id.list);

        dataEntries = URLDataDownload.getData();

        String[] values = new String[dataEntries.size()];
        int i=0;
        for(DataEntry dataEntry: dataEntries) {
            values[i] = dataEntry.getName();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert

                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                DataEntry selectedDataEntry = dataEntries.get(itemPosition);

                intentCall.setData(Uri.parse("tel://" + selectedDataEntry.getPhoneNumber() + "," + selectedDataEntry.getDtfmCode()));

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
