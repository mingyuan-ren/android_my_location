package com.example.mylocation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylocation.database.UserDBHelper;
import com.example.mylocation.databinding.ActivityMainBinding;
import com.example.mylocation.model.UserSelectedLocation;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Main Activity to display user selected locations, add new locations and sign out
 */
public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Button buttonNew;
    private Button buttonSave;
    private RecyclerView locationRecView;
    private String userName;
    private ArrayList<UserSelectedLocation> locationsRec = new ArrayList<>();
    private final UserDBHelper dbHelper;

    /**
     * Default constructor to initialize the database helper
     */
    public MainActivity() {
        dbHelper = new UserDBHelper(this);
    }

    /**
     * this method is used to initialize the main activity and set the necessary text and also display the user selected location and theme
     * @param savedInstanceState - activity instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize "add location" button, "location list" recycler view
        // and the userName which passed in by the UserLoginActivity
        buttonNew = findViewById(R.id.buttonAddLocation);
        buttonSave = findViewById(R.id.buttonSaveLocation);
        userName = getIntent().getStringExtra("username");
        this.setTitle("My Locations - " + userName);//set the user name displayed on top of the screen

        locationRecView = findViewById(R.id.locationRecView);
        initLocationData();//initialize location data for locationsRec
        LocationRecViewAdapter adapter = new LocationRecViewAdapter(this);
        adapter.setUserSelectedLocations(locationsRec);
        locationRecView.setAdapter(adapter);
        locationRecView.setLayoutManager(new LinearLayoutManager(this));

        //interactions with AddLocationActivity
        //set up button click listener, to receive the message(intent) sent back from AddLocationActivity
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddLocationActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        //when new location is added, put the new location in the user selected locations arraylist
        //and notify the adapter that there is data changed, so the shown list can be refreshed.
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String newLocationName = data.getExtras().getString("location").toString();
                    Double newLat = data.getExtras().getDouble("latitude");
                    Double newLng = data.getExtras().getDouble("longitude");
                    UserSelectedLocation newLocation = new UserSelectedLocation(newLocationName,
                            String.format(Locale.ENGLISH, "%f", newLat),
                            String.format(Locale.ENGLISH, "%f", newLng));
                    locationsRec.add(newLocation);
                    adapter.setUserSelectedLocations(locationsRec);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //save the current location list
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder userLocationsToSaveBuilder = new StringBuilder("");
                for(UserSelectedLocation l: locationsRec) {
                    userLocationsToSaveBuilder.append(l.serialize()).append("\t");
                }
                dbHelper.updateUserLocation(userName,userLocationsToSaveBuilder.toString());
                Toast.makeText(getBaseContext(), "The location list is saved!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Connect to the database and initialize the user location data.
     */
    private void initLocationData() {
        //connect to the database and fetch location list of the current user
        //find out the string of the locations stored for the user,
        //multiple locations are stored with split \t
        Cursor cursor = dbHelper.getLocations(userName);
        cursor.moveToFirst();
        String locationString = cursor.getString(0);

        //split the string into string array, and add the locations to the ArrayList
        // for later showing in recycler view
        if(locationString.length()>0) {
            String[] locationTemp = locationString.split("\t");
            for (String s : locationTemp) {
                locationsRec.add(new UserSelectedLocation(s));
            }
        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_on_main, menu);
        return true;
    }

    /**
     * save data when the users log out
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Logout from Main page. will jump to login page
        //And save the location list to the database when logging out
        int id = item.getItemId();
        //save current location list to the database, convert location names to a string, split by \t
        if (id == R.id.logoutItem) {
            //TODO: notify the user if she has not saved the list
            //jump to user login page
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * overriding the onDestroy method to close the opened database connection
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}