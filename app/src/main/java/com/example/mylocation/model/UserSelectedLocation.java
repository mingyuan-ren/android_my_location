package com.example.mylocation.model;

import java.io.Serializable;

/**
 * UserSelectedLocation - used to store user selected locations
 */
public class UserSelectedLocation implements Serializable {

    public String locationName;
    //adding the latitude and longitude details which will be used in future milestone requirements
    private String latitude;
    private String longitude;

    /**
     * Parameterized constructor for setting location name
     * @param locationName - location selected by user
     */
    public UserSelectedLocation(String locationName, String latitude, String longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude= longitude;

    }

    public UserSelectedLocation(String serializedLocation){
        String[] result = serializedLocation.split("\\|");
        if(result.length>=3) {
            this.locationName = result[0];
            this.latitude = result[1];
            this.longitude = result[2];
        }
    }

    /**
     * This method is used to fetch user selected location Name
     * @return - User selected location name
     */
    public String getLocationName() {
        return locationName;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    /**
     * This method is used to store user selected location name
     * @param locationName - name of user selected location
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * To String method for representing user selected locations as string
     * @return - string value of user selected locations
     */
    @Override
    public String toString() {
        return "UserSelectedLocation{" +
                "locationName='" + locationName + '\'' +
                '}';
    }

    public String serialize() {
        return locationName+"|"+latitude+"|"+longitude;
    }
}
