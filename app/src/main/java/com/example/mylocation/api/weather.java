//package com.example.mylocation.api;
//
//import static com.example.mylocation.BuildConfig.WEATHER_API_KEY;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Test Class
// */
//public class weather {
//
//    private String lat = "";
//    private String lon = "";
//    private JSONObject jo = null;
//
//    public weather(String lat, String lon) throws UnirestException, JSONException {
//        this.lat = lat;
//        this.lon = lon;
//        String request = String.format("https://api.weatherbit.io/v2.0/current?lat=%s&lon=%s&key=%s&include=minutely",lat, lon, WEATHER_API_KEY);
//        HttpResponse<JsonNode> response = Unirest.get(request).asJson();
//        JSONArray ja = (JSONArray) response.getBody().getObject().get("data");
//        JSONObject jo = (JSONObject) ja.get(0);
//        this.jo = jo;
//    }
//
//    public String getLat() {
//        return lat;
//    }
//
//    public String getLon() {
//        return lon;
//    }
//}
