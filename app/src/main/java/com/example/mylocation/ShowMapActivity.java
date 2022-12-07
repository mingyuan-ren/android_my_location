package com.example.mylocation;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity for showing map within the app
 */
public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double initLng;
    private Double initLat;
    private String cityName;
    private TextView map_location_name;
    private TextView map_location_latlng;

    /**
     * this method is used to initialize the show map activity and set the necessary text and also display the user selected location name, lat and long
     * @param savedInstanceState - activity instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_map);

        //add a map and set all necessary elements like zoom control handles
        GoogleMapOptions options = new GoogleMapOptions();

        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .zoomControlsEnabled(true)
                .compassEnabled(true)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);

        map_location_name = findViewById(R.id.map_location_name);
        map_location_latlng = findViewById(R.id.map_location_latlng);

        Intent curIntent = getIntent();
        initLng = Double.parseDouble(curIntent.getExtras().getString("longitude"));
        initLat = Double.parseDouble(curIntent.getExtras().getString("latitude"));
        cityName = curIntent.getExtras().getString("city");

        map_location_name.setText("Location Name: "+cityName);
        map_location_latlng.setText("Latitude: "+initLat.toString()+", Longitude: "+initLng.toString());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * @param googleMap - the map we get from google map sdk
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng city = new LatLng(initLat, initLng);
        mMap.addMarker(new MarkerOptions().position(city).title(cityName));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 15f));

    }
}