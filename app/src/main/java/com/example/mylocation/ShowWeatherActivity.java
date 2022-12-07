package com.example.mylocation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.example.mylocation.databinding.ActivityShowWeatherBinding;

public class ShowWeatherActivity extends AppCompatActivity {

    private TextView city_name;
    private TextView date_time;
    private TextView temperature;
    private ImageView weather_image;
    private TextView condition;
    private TextView humidity;
    private TextView wind;
    private ImageView background;
    private String cityName;
    private ActivityShowWeatherBinding binding;
    private CardView cardViewMain;
    private CardView cardViewSecondary;
    private ProgressBar spinner;

    /**
     * this method is used to initialize the show weather activity, set and display the necessary texts
     * @param savedInstanceState - activity instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        binding = ActivityShowWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        city_name = findViewById(R.id.city_name_id);
        date_time = findViewById(R.id.date_and_time_id);
        temperature = findViewById(R.id.temperature_id);
        weather_image = findViewById(R.id.weather_image);
        condition = findViewById(R.id.condition_id);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
        background = findViewById(R.id.background);
        cardViewMain = findViewById(R.id.main_card_view);
        cardViewSecondary = findViewById(R.id.secondary_card_view);
        spinner = findViewById(R.id.progressBar);

        Intent curIntent = getIntent();
        cityName = curIntent.getExtras().getString("city");
        try  {
            getWeatherInfo(cityName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is to get the detailed weather info from the weather api
     * TODO break down method into smaller methods to reduce complexity
     * @param inputCityName - the city name that asks for the weather info
     */
    private void getWeatherInfo(String inputCityName) throws JSONException, IOException {
        String weatherKey = getResources().getString(R.string.WEATHER_API_KEY);
        String url = "http://api.weatherapi.com/v1/current.json?key=" + weatherKey + "&q=" + inputCityName + "&aqi=no";
        city_name.setText(inputCityName);
        RequestQueue requestQueue = Volley.newRequestQueue(ShowWeatherActivity.this);

        JsonObjectRequest weatherInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject jsonLoc = response.getJSONObject("location");
                            String name = jsonLoc.getString("name");
                            city_name.setText(name);
                            //calculate time using time zone
                            String timeZone = jsonLoc.getString("tz_id");
                            Instant now = Instant.now();
                            ZoneId zoneId = ZoneId.of(timeZone);
                            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                            String time = zonedDateTime.format(formatter);
                            date_time.setText(time);

                            JSONObject jsonCurr = response.getJSONObject("current");
                            int isDay = jsonCurr.getInt("is_day");
                            String dayBack = "https://images.unsplash.com/photo-1594626584089-c3f8c25b1711?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=735&q=80";
                            String nightBack = "https://images.unsplash.com/photo-1435224654926-ecc9f7fa028c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80";
                            if (isDay == 1) {
                                loadImage(dayBack, background);
                            } else {
                                loadImage(nightBack, background);
                            }
                            String temp_c = jsonCurr.getString("temp_c");
                            String temp_f = jsonCurr.getString("temp_f");
                            String temp = temp_c + "°C / " + temp_f + "°F";
                            temperature.setText(temp);
                            String conditionText = jsonCurr.getJSONObject("condition").getString("text");
                            String conditionIcon = jsonCurr.getJSONObject("condition").getString("icon");
                            String conditionUrl = "http:".concat(conditionIcon);
                            loadImage(conditionUrl, weather_image);
                            condition.setText(conditionText);

                            String humidityString = jsonCurr.getString("humidity");
                            humidity.setText("Humidity: " + humidityString);

                            String windDir = jsonCurr.getString("wind_dir");
                            String windDegree = jsonCurr.getString("wind_degree");
                            wind.setText("Wind: " + windDegree + " " + windDir);
                            spinner.setVisibility(View.INVISIBLE);
                            city_name.setVisibility(View.VISIBLE);
                            cardViewMain.setVisibility(View.VISIBLE);
                            cardViewSecondary.setVisibility(View.VISIBLE);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowWeatherActivity.this, "Error while loading weather details, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(weatherInfo);
    }

    /**
     * this method is to get the image for the background and icon
     * @param url - the url of the image
     * @param place - the place to thow the image in the app
     */
    private void loadImage(String url, ImageView place) {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                Picasso.get().load(url).into(place);
            }
        });
    }
}