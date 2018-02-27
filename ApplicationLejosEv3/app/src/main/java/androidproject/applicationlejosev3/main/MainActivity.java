package androidproject.applicationlejosev3.main;

/**
 * MainList.java
 * Created: September 11, 2016
 * Colin FRAPPER
 */
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidproject.applicationlejosev3.connection.ConnectWifiActivity;
import androidproject.applicationlejosev3.R;
import androidproject.applicationlejosev3.connection.ConnectionBluetoothActivity;


public class MainActivity extends ListActivity {

    private List<String> activities = new ArrayList<String>();
    private Map<String, Class> name2class = new HashMap<String, Class>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Add Activities to list */
        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, activities));

        /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());
    }


    /* Private Help Entities */
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
            String activity_name = activities.get(position);
            Class activity_class = name2class.get(activity_name);

    		/* Start new Activity */
            Intent intent = new Intent(MainActivity.this, activity_class);
            MainActivity.this.startActivity(intent);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setup_activities() {
        addActivity("Connexion avec Bluetooth", ConnectionBluetoothActivity.class);
        addActivity("Connexion avec Wifi", ConnectWifiActivity.class);
    }

    private void addActivity(String name, Class activity) {
        activities.add(name);
        name2class.put(name, activity);
    }



    }
