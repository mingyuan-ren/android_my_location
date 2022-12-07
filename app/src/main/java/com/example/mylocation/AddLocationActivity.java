package com.example.mylocation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

/**
 * Used for adding new location
 */
public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener {

    //elements on the AddLocationActivity page
    EditText search_location_text;
    AutoCompleteTextView search_auto_complete;
    Button add_location_clear;
    Button add_location_confirm;
    List<Place.Field> fields;
    Intent search_intent;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private double curLng = 0.0;
    private double curLat = 0.0;

    /**
     * This method is used to create the add location activity and initialize the google places autocomplete api
     * @param savedInstanceState - activity instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        search_location_text = findViewById(R.id.search_location_text);
        add_location_clear = findViewById(R.id.add_location_clear);
        add_location_confirm = findViewById(R.id.add_location_confirm);
        add_location_confirm.setOnClickListener(this);
        add_location_clear.setOnClickListener(this);
        fields = Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        search_intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.API_KEY));
        }
        search_location_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(search_intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        search_location_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) startActivityForResult(search_intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

    }

    /**
     * This method is used to fetch the selected location and display it to user for confirmation
     * @param requestCode - autocomplete request code
     * @param resultCode - result code of the autocomplete request
     * @param data - intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng =  place.getLatLng();
                if(latLng!=null) {
                    curLat = latLng.latitude;
                    curLng = latLng.longitude;
                }
                search_location_text.setText(place.getName());
            }
        }
    }

    /**
     * This method is used to confirm selected location or otherwise clear the selected location
     * @param view - current view
     */
    public void onClick(View view) {
        Intent intent;
        if(view.getId()==R.id.add_location_confirm) {//pass the name of the location back to MainActivity
            EditText location = (EditText) findViewById(R.id.search_location_text);
            String message = location.getText().toString();
            if(message.length()>0) {
                intent = new Intent();
                intent.putExtra("location",message);
                intent.putExtra("longitude", curLng);
                intent.putExtra("latitude", curLat);
                setResult(RESULT_OK, intent);
                finish();
                //TODO: save data to database
            }
            else Toast.makeText(this, "You have not entered anything!", Toast.LENGTH_SHORT).show();
        }
        else {//clear the edit text (remove the input)
            search_location_text.getText().clear();
        }
    }
}